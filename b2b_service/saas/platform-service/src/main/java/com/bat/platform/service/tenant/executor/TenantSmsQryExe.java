package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.tenant.dto.TenantSmsQry;
import com.bat.platform.api.tenant.dto.data.TenantSmsDTO;
import com.bat.platform.dao.tenant.PlatformTenantSmsMapper;
import com.bat.platform.dao.tenant.PlatformTenantSmsTemplateMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsDO;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantSmsTemplateDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 沙漠
 */
@Component
public class TenantSmsQryExe {

    @Resource
    private PlatformTenantSmsMapper platformTenantSmsMapper;

    @Resource
    private PlatformTenantSmsTemplateMapper platformTenantSmsTemplateMapper;

    public TenantSmsDTO config(TenantSmsQry qry) {
        PlatformTenantSmsDO platformTenantSmsDO = platformTenantSmsMapper.selectByTenantIdAndSmsType(qry.getTenantId(), qry.getSmsType());
        if (platformTenantSmsDO == null) {
            return null;
        }
        TenantSmsDTO tenantSmsDTO = new TenantSmsDTO();
        BeanUtils.copyProperties(platformTenantSmsDO, tenantSmsDTO);
        List<PlatformTenantSmsTemplateDO> list = platformTenantSmsTemplateMapper.listByPlatformTenantSmsId(platformTenantSmsDO.getId());
        List<TenantSmsDTO.TenantSmsTemplate> tenantSmsTemplateList = new ArrayList<>();
        for (PlatformTenantSmsTemplateDO platformTenantSmsTemplateDO : list) {
            TenantSmsDTO.TenantSmsTemplate tenantSmsTemplate = new TenantSmsDTO.TenantSmsTemplate();
            BeanUtils.copyProperties(platformTenantSmsTemplateDO, tenantSmsTemplate);
            tenantSmsTemplateList.add(tenantSmsTemplate);
        }
        tenantSmsDTO.setTenantSmsTemplates(tenantSmsTemplateList);
        return tenantSmsDTO;
    }
}
