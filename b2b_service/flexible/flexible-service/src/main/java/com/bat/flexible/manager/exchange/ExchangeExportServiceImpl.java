package com.bat.flexible.manager.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeExportCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeExportQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.api.exchange.ExchangeCardServiceI;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.ExchangeExportServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeExcelDTO;
import com.bat.flexible.api.exchange.dto.ExchangeExportCmd;
import com.bat.flexible.api.exchange.dto.ExchangeExportPageQry;
import com.bat.flexible.dao.exchange.co.ExchangeExportPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeExportCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeExportDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.common.utils.IOUtils;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.common.utils.excel.ExcelFileUtils;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Service
public class ExchangeExportServiceImpl implements ExchangeExportServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeExportServiceImpl.class);

    @Autowired
    private ExchangeExportQryExe exchangeExportQryExe;

    @Autowired
    private ExchangeExportCmdExe exchangeExportCmdExe;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @DubboReference(check = false, timeout = 50000, retries = 0)
    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;

    @CreateCache(name = FlexibleKeyConstant.EXCHANGE_EXPORT_OUT_PRE)
    private Cache<String, Integer> exchangeExportOutCache;

    @Override
    public PageInfo<List<ExchangeExportPageCO>> page(ExchangeExportPageQry exchangeExportPageQry) {
        PageHelper.startPage(exchangeExportPageQry.getPage(), exchangeExportPageQry.getSize());
        List<ExchangeExportPageCO> list = exchangeExportQryExe.listCOByCondition(exchangeExportPageQry.getExchangeId());
        return new PageInfo(list);
    }

    @Override
    public Response add(ExchangeExportCmd exchangeExportCmd) {
        ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeExportCmd.getExchangeId());
        if (exchangeCardDO.getStatus() - ExchangeConstant.StatusEnd == 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CREATE_CODE_FAIL_BY_ACTIVITY_END);
        }
        if (exchangeCardDO.getIsEntity() == FlexibleCommonConstant.FLAG_YES) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_ENTITY_CANT_EXPORT);
        }
        if (exchangeExportCmd.getOutNum() <= 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_EXPORT_NUM_ERROR);
        }
        exchangeExportCmdExe.add(exchangeExportCmd);
        return Response.buildSuccess();
    }

    @Override
    public Response update(ExchangeExportCmd exchangeExportCmd) {
        ExchangeExportDO exchangeExportDO = new ExchangeExportDO();
        BeanUtils.copyProperties(exchangeExportCmd, exchangeExportDO);
        exchangeExportCmdExe.update(exchangeExportDO);
        return Response.buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<String> export(Integer id, AdminResponse currentAdmin) {
        ExchangeExportDO exchangeExportDO = exchangeExportQryExe.getById(id);
        if (exchangeExportDO == null) {
            throw new FlexibleCustomException("找不到导出记录!");
        }
        if (exchangeExportDO.getExamineFlag() != FlexibleCommonConstant.FLAG_YES) {
            throw new FlexibleCustomException("需要审核后才能导出!");
        }
        if (StringUtils.isNotBlank(exchangeExportDO.getFileUrl())) {
            return Response.of(exchangeExportDO.getFileUrl());
        }
        // 分布式锁
        AutoReleaseLock autoReleaseLock =
            exchangeExportOutCache.tryLock(TenantContext.getTenantNo() + ":" + "0", 3, TimeUnit.SECONDS);
        if (autoReleaseLock == null) {
            // 加锁
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_HAS_EXPORT);
        }
        try {
            List<ExchangeCodeDO> exchangeCodeDOS = exchangeCodeQryExe.listByExportId(exchangeExportDO.getId());
            if (exchangeCodeDOS.size() == 0) {
                // 得到生成的兑换码
                exchangeCodeDOS = exchangeCodeServiceI.createEleActivateCode(exchangeExportDO.getExchangeId(),
                    exchangeExportDO.getDistributorId(), exchangeExportDO.getOutNum(), currentAdmin);
            }
            ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeExportDO.getExchangeId());
            List<ExchangeExportCodeDO> exchangeExportCodeDOS = new ArrayList<>();
            List<ExchangeCodeExcelDTO> exchangeCodeExcelDTOS = new ArrayList<>();
            for (ExchangeCodeDO exchangeCodeDO : exchangeCodeDOS) {
                ExchangeCodeExcelDTO exchangeCodeExcelDTO = new ExchangeCodeExcelDTO();
                BeanUtils.copyProperties(exchangeCodeDO, exchangeCodeExcelDTO);
                exchangeCodeExcelDTO.setCodeName(exchangeCardDO.getCodeName());
                exchangeCodeExcelDTO.setSecretCode(AESUtil.decrypt(exchangeCodeDO.getSecretCode()));
                exchangeCodeExcelDTO.setExchangeExportId(exchangeExportDO.getId());
                exchangeCodeExcelDTOS.add(exchangeCodeExcelDTO);
                ExchangeExportCodeDO exchangeExportCodeDO = new ExchangeExportCodeDO();
                exchangeExportCodeDO.setExchangeExportId(exchangeExportDO.getId());
                exchangeExportCodeDO.setExchangeCodeId(exchangeCodeDO.getId());
                exchangeExportCodeDOS.add(exchangeExportCodeDO);
            }
            exchangeExportCmdExe.addExportCodes(exchangeExportCodeDOS);
            File file = ExcelFileUtils.create(exchangeCodeExcelDTOS, ExchangeCodeExcelDTO.class, "excel", "xlsx");
            String filePath = "";
            InputStream inputStream = null;
            try {
                LOGGER.info("开始上传excel到oss");
                // 将文件推送到oos
                StringBuffer fileNameBuffer = new StringBuffer();
                fileNameBuffer.append(exchangeExportDO.getId()).append("/").append(exchangeExportDO.getExchangeId())
                    .append("/").append(exchangeExportDO.getDistributorId()).append("/")
                    .append(System.currentTimeMillis()).append("/").append(file.getName());
                LOGGER.info("即将上传的路径:{}", fileNameBuffer);
                inputStream = new FileInputStream(file);
                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(inputStream);
                } catch (IOException e) {
                    LOGGER.info("获取字节流出现异常:{}", e.getMessage());
                    throw new FlexibleCustomException("获取字节流出现异常:" + e.getMessage());
                }
                filePath = thirdPartySystemOssServiceRpc.uploadExtendStream(fileNameBuffer.toString(), "excel/", bytes);
                LOGGER.info("上传回来的文件路径{}", filePath);
            } catch (Exception e) {
                LOGGER.error("excel文件上传失败:{}", e.getMessage());
                throw new FlexibleCustomException("excel文件上传失败、记录id" + exchangeExportDO.getId());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    LOGGER.error("关闭流出现异常:{}", e.getMessage());
                }
            }
            exchangeExportDO.setFileUrl(filePath);
            exchangeExportCmdExe.update(exchangeExportDO);
            // 将本地文件进行移除
            file.delete();
            return Response.of(filePath);
        } finally {
            autoReleaseLock.close();
        }
    }

    @Override
    public void delete(Integer id) {
        exchangeExportCmdExe.delete(id);
    }
}
