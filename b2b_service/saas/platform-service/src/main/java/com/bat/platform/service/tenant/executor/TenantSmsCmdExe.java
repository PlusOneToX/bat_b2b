package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantSmsCmd;
import com.bat.platform.dao.tenant.PlatformTenantSmsMapper;
import com.bat.platform.dao.tenant.PlatformTenantSmsTemplateMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsDO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsTemplateDO;
import com.bat.platform.service.common.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 沙漠
 */
@Component
public class TenantSmsCmdExe {

    @Resource
    private PlatformTenantSmsMapper platformTenantSmsMapper;

    @Resource
    private PlatformTenantSmsTemplateMapper platformTenantSmsTemplateMapper;


    public void add(TenantSmsCmd cmd) {
        PlatformTenantSmsDO platformTenantSmsDO = platformTenantSmsMapper.selectByTenantIdAndSmsType(cmd.getTenantId(), cmd.getSmsType());
        if (platformTenantSmsDO != null) {
            cmd.setId(platformTenantSmsDO.getId());
            cmd.setCreateTime(platformTenantSmsDO.getCreateTime());
            update(cmd);
            return;
        }
        platformTenantSmsDO = new PlatformTenantSmsDO();
        BeanUtils.copyProperties(cmd, platformTenantSmsDO);
        Date date = new Date();
        platformTenantSmsDO.setCreateTime(date);
        platformTenantSmsDO.setUpdateTime(date);
        platformTenantSmsMapper.insert(platformTenantSmsDO);
        if(cmd.getTenantSmsTemplates()==null||cmd.getTenantSmsTemplates().size()==0){
            throw PlatformException.buildException(ErrorCode.B_PLATFORM_MUST_ADD_ONE_TEMPLATE);
        }
        for( TenantSmsCmd.TenantSmsTemplate template :cmd.getTenantSmsTemplates()){
            template.setPlatformTenantSmsId(platformTenantSmsDO.getId());
            addTemplate(template);
        }
    }

    public void update(TenantSmsCmd cmd) {
        PlatformTenantSmsDO platformTenantSmsDO = new PlatformTenantSmsDO();
        BeanUtils.copyProperties(cmd, platformTenantSmsDO);
        platformTenantSmsDO.setUpdateTime(new Date());
        if (platformTenantSmsDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantSmsMapper.updateByPrimaryKey(platformTenantSmsDO);
        if(cmd.getTenantSmsTemplates()==null||cmd.getTenantSmsTemplates().size()==0){
            throw PlatformException.buildException(ErrorCode.B_PLATFORM_MUST_ADD_ONE_TEMPLATE);
        }
        for( TenantSmsCmd.TenantSmsTemplate template :cmd.getTenantSmsTemplates()){
            template.setPlatformTenantSmsId(platformTenantSmsDO.getId());
            addTemplate(template);
        }
    }

    private void addTemplate(TenantSmsCmd.TenantSmsTemplate template) {
        PlatformTenantSmsTemplateDO platformTenantSmsTemplateDO = platformTenantSmsTemplateMapper.selectByPlatformTenantSmsIdAndTemplateType(template.getPlatformTenantSmsId(),template.getTemplateType());
        if (platformTenantSmsTemplateDO != null) {
            template.setId(platformTenantSmsTemplateDO.getId());
            updateTemplate(template);
            return;
        }
        platformTenantSmsTemplateDO = new PlatformTenantSmsTemplateDO();
        BeanUtils.copyProperties(template, platformTenantSmsTemplateDO);
        platformTenantSmsTemplateMapper.insert(platformTenantSmsTemplateDO);
    }

    private void updateTemplate(TenantSmsCmd.TenantSmsTemplate template) {
        PlatformTenantSmsTemplateDO platformTenantSmsDO = new PlatformTenantSmsTemplateDO();
        BeanUtils.copyProperties(template, platformTenantSmsDO);
        if (platformTenantSmsDO.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        platformTenantSmsTemplateMapper.updateByPrimaryKey(platformTenantSmsDO);
    }
}
