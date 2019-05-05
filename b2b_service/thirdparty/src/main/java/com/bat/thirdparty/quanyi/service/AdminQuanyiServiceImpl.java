package com.bat.thirdparty.quanyi.service;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.quanyi.QuanyiErrorCode;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiCmdExe;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiQryExe;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeDecryptRpcDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeStatusCmd;
import com.bat.dubboapi.flexible.third.api.ThirdSkuServiceRpc;
import com.bat.thirdparty.quanyi.api.AdminQuanyiServiceI;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiCancelCmd;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiLogQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiUnCancelCmd;
import com.bat.thirdparty.quanyi.common.QuanyiConstant;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiLogPageCO;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiPageCO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AdminQuanyiServiceImpl implements AdminQuanyiServiceI {

    @Autowired
    private ThirdQuanyiCmdExe thirdQuanyiCmdExe;

    @Value("${suning.distributor.id}")
    private Integer suNingDistributorId;

    @DubboReference(check = false,timeout = 50000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false,timeout = 50000)
    private ThirdSkuServiceRpc thirdSkuServiceRpc;

    @Autowired
    private ThirdQuanyiQryExe thirdQuanyiQryExe;

    @DubboReference(check = false,timeout = 50000,retries = 0)
    private ExchangeCodeServiceRpc exchangeCodeServiceRpc;

    @CreateCache(name = ThirdKeyConstant.EXCHANGE_QUANYI_PRE)
    private Cache<String, Integer> exchangeQuanyiCache;



    @Override
    public PageInfo<List<ThirdQuanyiPageCO>> page(ThirdQuanyiQry thirdQuanyiQry) {
        PageHelper.startPage(thirdQuanyiQry.getPage(), thirdQuanyiQry.getSize());

        List<ThirdQuanyiDO> list = thirdQuanyiQryExe.listByCondition(thirdQuanyiQry.getDistributorId(), thirdQuanyiQry.getDistributorName(),
                thirdQuanyiQry.getSecretCode(), thirdQuanyiQry.getThirdCode(), thirdQuanyiQry.getThirdPhone(), thirdQuanyiQry.getStatus());

        PageInfo pageInfo = new PageInfo(list);
        List<ThirdQuanyiPageCO> thirdQuanyiPageCOS = new ArrayList<>();
        //兑换码列表
        List<String> encryptSecretCodes = new ArrayList<>();
        for (ThirdQuanyiDO thirdQuanyiDO : list) {
            if (StringUtils.isNotBlank(thirdQuanyiDO.getSecretCode())) {
                encryptSecretCodes.add(thirdQuanyiDO.getSecretCode());
            }
        }
        Map<String, String> decryptSecretCodeMap = new HashMap<>();
        if (encryptSecretCodes.size() > 0) {
            Response<List<ExchangeCodeDecryptRpcDTO>> response = exchangeCodeServiceRpc.decryptSecretCodes(encryptSecretCodes);
            if (response.isSuccess() && response.getData() != null) {
                List<ExchangeCodeDecryptRpcDTO> exchangeCodeDecrypts = response.getData();
                decryptSecretCodeMap = exchangeCodeDecrypts.stream().collect(Collectors.toMap(ExchangeCodeDecryptRpcDTO::getEncryptSecretCode, ExchangeCodeDecryptRpcDTO::getDecryptSecretCode, (k1, k2) -> k1));
            }
        }

        for (ThirdQuanyiDO thirdQuanyiDO : list) {
            ThirdQuanyiPageCO thirdQuanyiPageCO = new ThirdQuanyiPageCO();
            BeanUtils.copyProperties(thirdQuanyiDO, thirdQuanyiPageCO);
            thirdQuanyiPageCO.setStatus(getStatus(thirdQuanyiDO));
            if (thirdQuanyiDO.getExchangeCodeId() != null) {
                String decryptSecretCode = decryptSecretCodeMap.get(thirdQuanyiDO.getSecretCode());
                if (StringUtils.isNotBlank(decryptSecretCode)) {
                    thirdQuanyiPageCO.setSecretCode(decryptSecretCode);
                }
            }
            thirdQuanyiPageCOS.add(thirdQuanyiPageCO);
        }
        pageInfo.setList(thirdQuanyiPageCOS);
        return pageInfo;
    }

    //状态 1.未验证 2.未兑换 3.已兑换 4已作废
    private Short getStatus(ThirdQuanyiDO thirdQuanyi) {
        if (thirdQuanyi.getCancelFlag() != null && thirdQuanyi.getCancelFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
            return QuanyiConstant.status4;
        }
        if (thirdQuanyi.getDestroyFlag() != null && thirdQuanyi.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
            return QuanyiConstant.status3;
        }
        if (thirdQuanyi.getExchangeCodeId() != null) {
            return QuanyiConstant.status2;
        }
        return QuanyiConstant.status1;
    }

    @Override
    public PageInfo<List<ThirdQuanyiLogPageCO>> logPage(ThirdQuanyiLogQry thirdQuanyiLogQry) {
        PageHelper.startPage(thirdQuanyiLogQry.getPage(), thirdQuanyiLogQry.getSize());
        List<ThirdQuanyiLogDO> list = thirdQuanyiQryExe.logByThirdQuanyiId(thirdQuanyiLogQry.getThirdQuanyiId());
        PageInfo pageInfo = new PageInfo(list);
        List<ThirdQuanyiLogPageCO> thirdQuanyiLogPageCOS = new ArrayList<>();
        for (ThirdQuanyiLogDO thirdQuanyiLogDO : list) {
            ThirdQuanyiLogPageCO thirdQuanyiPageCO = new ThirdQuanyiLogPageCO();
            BeanUtils.copyProperties(thirdQuanyiLogDO, thirdQuanyiPageCO);
            thirdQuanyiLogPageCOS.add(thirdQuanyiPageCO);
        }
        pageInfo.setList(thirdQuanyiLogPageCOS);
        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(ThirdQuanyiCancelCmd cmd) {
        AutoReleaseLock autoReleaseLock = null;
        try {
            // 分布式锁
            autoReleaseLock =
                    exchangeQuanyiCache.tryLock(TenantContext.getTenantNo() + ":" + cmd.getId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                // 加锁
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
            }
            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(cmd.getId());
            if (thirdQuanyiDO == null) {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_ID_ERROR);
            }
            if (thirdQuanyiDO.getCancelFlag() != null && thirdQuanyiDO.getCancelFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_HAS_CANCEL);
            }
            if (thirdQuanyiDO.getDestroyFlag() != null && thirdQuanyiDO.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_HAS_DESTROY_CANNOT_CANCEL);
            }
            if (StringUtils.isBlank(cmd.getReason())) {
                cmd.setReason(QuanyiConstant.invalidReason);
            }
            thirdQuanyiDO.setCancelFlag(ThirdCommonConstant.COMMON_FLAG_YES);
            thirdQuanyiDO.setCancelTime(new Date());
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
            if (thirdQuanyiDO.getExchangeCodeId() != null) {
                ExchangeCodeStatusCmd exchangeCodeStatusCmd = new ExchangeCodeStatusCmd();
                exchangeCodeStatusCmd.setId(thirdQuanyiDO.getExchangeCodeId());
                exchangeCodeStatusCmd.setDistributorId(cmd.getDistributorId());
                exchangeCodeStatusCmd.setDistributorName(cmd.getDistributorName());
                exchangeCodeStatusCmd.setReason(cmd.getReason());
                Response response = exchangeCodeServiceRpc.invalid(exchangeCodeStatusCmd);
                if (!response.isSuccess()) {
                    throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
                }
            }
            ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
            thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiDO.getId());
            thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType3);
            thirdQuanyiLogDO.setOperatorId(cmd.getDistributorId());
            thirdQuanyiLogDO.setOperatorName(cmd.getDistributorName());
            thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType7);
            thirdQuanyiLogDO.setRemark(cmd.getReason());
            thirdQuanyiLogDO.setCreateTime(new Date());
            thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
        }finally {
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unCancel(ThirdQuanyiUnCancelCmd cmd) {
        AutoReleaseLock autoReleaseLock = null;
        try {
            // 分布式锁
            autoReleaseLock =
                    exchangeQuanyiCache.tryLock(TenantContext.getTenantNo() + ":" + cmd.getId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                // 加锁
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
            }
            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(cmd.getId());
            if (thirdQuanyiDO == null) {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_ID_ERROR);
            }
            if (thirdQuanyiDO.getDestroyFlag() != null && thirdQuanyiDO.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_HAS_DESTROY_CANNOT_CANCEL);
            }
            if (StringUtils.isBlank(cmd.getReason())) {
                cmd.setReason(QuanyiConstant.unInvalidReason);
            }
            thirdQuanyiDO.setCancelFlag(ThirdCommonConstant.COMMON_FLAG_NO);
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
            if (thirdQuanyiDO.getExchangeCodeId() != null) {
                ExchangeCodeStatusCmd exchangeCodeStatusCmd = new ExchangeCodeStatusCmd();
                exchangeCodeStatusCmd.setId(thirdQuanyiDO.getExchangeCodeId());
                exchangeCodeStatusCmd.setDistributorId(cmd.getDistributorId());
                exchangeCodeStatusCmd.setDistributorName(cmd.getDistributorName());
                Response response = exchangeCodeServiceRpc.unInvalid(exchangeCodeStatusCmd);
                if (!response.isSuccess()) {
                    throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
                }
            }
            ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
            thirdQuanyiLogDO.setThirdQuanyiId(thirdQuanyiDO.getId());
            thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType3);
            thirdQuanyiLogDO.setOperatorId(cmd.getDistributorId());
            thirdQuanyiLogDO.setOperatorName(cmd.getDistributorName());
            thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType8);
            thirdQuanyiLogDO.setRemark(cmd.getReason());
            thirdQuanyiLogDO.setCreateTime(new Date());
            thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
        }finally {
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
