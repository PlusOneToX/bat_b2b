package com.bat.platform.service.tenant.executor;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantQry;
import com.bat.platform.api.tenant.dto.data.TenantDTO;
import com.bat.platform.api.tenant.dto.data.TenantSummaryDTO;
import com.bat.platform.dao.tenant.*;
import com.bat.platform.dao.tenant.dataobject.*;
import com.bat.platform.service.common.CommonErrorCode;
import com.bat.platform.service.tenant.TenantServiceRpcImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/29 11:27
 */
@Component
public class TenantQryExe {

    @Resource
    private PlatformTenantMapper platformTenantMapper;

    @Resource
    private PlatformTenantCommonMapper platformTenantCommonMapper;

    @Resource
    private PlatformTenantDBMapper platformTenantDBMapper;

    @Resource
    private PlatformTenantDiyFactoryMapper platformTenantDiyFactoryMapper;

    @Resource
    private PlatformTenantErpMapper platformTenantErpMapper;

    @Resource
    private PlatformTenantOssMapper platformTenantOssMapper;

    @Resource
    private PlatformTenantSmsMapper platformTenantSmsMapper;

    @Resource
    private PlatformTenantSmsTemplateMapper platformTenantSmsTemplateMapper;

    @Resource
    private PlatformTenantUrlMapper platformTenantUrlMapper;

    private static final Logger log = LoggerFactory.getLogger(TenantQryExe.class);



    public PageInfo<TenantDTO> list(TenantQry qry) {
        List<PlatformTenantDO> list = platformTenantMapper.listCOByCondition(qry.getContentType(), qry.getContent());
        PageInfo pageInfo = new PageInfo(list);
        List<TenantDTO> tenantDTOS = new ArrayList<>();
        for (PlatformTenantDO platformTenantDO : list) {
            TenantDTO tenantDTO = new TenantDTO();
            BeanUtils.copyProperties(platformTenantDO, tenantDTO);
            tenantDTOS.add(tenantDTO);
        }
        pageInfo.setList(tenantDTOS);
        return pageInfo;
    }

    public TenantDTO detail(Integer id) {
        PlatformTenantDO platformTenantDO = platformTenantMapper.selectByPrimaryKey(id);
        if (platformTenantDO == null) {
            return null;
        }
        TenantDTO tenantDTO = new TenantDTO();
        BeanUtils.copyProperties(platformTenantDO, tenantDTO);
        return tenantDTO;
    }

    public TenantSummaryDTO summary(Integer id) {
        //判断基础信息配置是否配置好
        boolean commonFlag = true;
        PlatformTenantCommonDO platformTenantCommonDO = platformTenantCommonMapper.selectByTenantId(id);
        if (platformTenantCommonDO == null) {
            commonFlag = false;
        }
        List<PlatformTenantUrlDO> platformTenantUrlDOS = platformTenantUrlMapper.listByTenantId(id);
        if (platformTenantUrlDOS.size() == 0) {
            commonFlag = false;
        }
        //判断数据库配置是否配置好
        boolean dbFlag = true;
        List<PlatformTenantDBDO> platformTenantDBDOS = platformTenantDBMapper.listByTenantId(id);
        if (platformTenantDBDOS.size() == 0) {
            dbFlag = false;
        }
        //判断定制信息配置是否配置好
        boolean diyFlag = true;
        List<PlatformTenantDiyFactoryDO> platformTenantDiyFactoryDOS = platformTenantDiyFactoryMapper.listByTenantId(id);
        if (platformTenantDiyFactoryDOS.size() == 0) {
            diyFlag = false;
        }
        //判断ERP信息配置是否配置好
        boolean erpFlag = true;
        PlatformTenantErpDO platformTenantErpDO = platformTenantErpMapper.selectByTenantId(id);
        if (platformTenantErpDO == null) {
            erpFlag = false;
        }
        //判断文件存储配置信息是否配置好
        boolean ossFlag = true;
        List<PlatformTenantOssDO> platformTenantOssDOS = platformTenantOssMapper.listByTenantId(id);
        if (platformTenantOssDOS.size() == 0) {
            ossFlag = false;
        }
        //判断短信配置信息是否配置好
        boolean smsFlag=true;
        List<PlatformTenantSmsDO> platformTenantSmsDOS=platformTenantSmsMapper.listByTenantId(id);
        if(platformTenantSmsDOS.size()==0){
            smsFlag=false;
        }else{
            List<Integer> platformTenantSmsIds = platformTenantSmsDOS.stream().map(a -> a.getId()).collect(Collectors.toList());
            List<PlatformTenantSmsTemplateDO> platformTenantSmsTemplateDOS=   platformTenantSmsTemplateMapper.listByPlatformTenantSmsIds(platformTenantSmsIds);
           if(platformTenantSmsTemplateDOS.size()==0){
               smsFlag=false;
           }
        }
        TenantSummaryDTO tenantSummaryDTO=new TenantSummaryDTO();
        tenantSummaryDTO.setCommonFlag(commonFlag);
        tenantSummaryDTO.setDbFlag(dbFlag);
        tenantSummaryDTO.setDiyFlag(diyFlag);
        tenantSummaryDTO.setErpFlag(erpFlag);
        tenantSummaryDTO.setOssFlag(ossFlag);
        tenantSummaryDTO.setSmsFlag(smsFlag);
        return tenantSummaryDTO;
    }

    public Integer getTenantIdByTenantNo(String tenantNo) {
        log.info("租户编码:{}",tenantNo);
        PlatformTenantDO platformTenantDO = platformTenantMapper.selectByTenantNo(tenantNo);
        if(platformTenantDO==null){
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
        }
        return platformTenantDO.getId();
    }

    public TenantDTO getDetailByTenantNo(String tenantNo) {
        PlatformTenantDO platformTenantDO = platformTenantMapper.selectByTenantNo(tenantNo);
        if (platformTenantDO == null) {
            return null;
        }
        TenantDTO tenantDTO = new TenantDTO();
        BeanUtils.copyProperties(platformTenantDO, tenantDTO);
        return tenantDTO;
    }
}
