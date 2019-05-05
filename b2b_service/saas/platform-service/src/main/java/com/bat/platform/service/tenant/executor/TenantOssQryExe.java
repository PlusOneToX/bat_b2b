package com.bat.platform.service.tenant.executor;

import com.bat.platform.api.tenant.dto.TenantOssQry;
import com.bat.platform.api.tenant.dto.data.TenantOssDTO;
import com.bat.platform.dao.tenant.PlatformTenantOssMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantOssDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 沙漠
 */
@Component
public class TenantOssQryExe {

    @Resource
    private PlatformTenantOssMapper platformTenantOssMapper;


    public TenantOssDTO config(TenantOssQry qry) {
        PlatformTenantOssDO platformTenantOssDO = platformTenantOssMapper.findByTenantIdAndOssType(qry.getTenantId(), qry.getOssType());
        if (platformTenantOssDO == null) {
            return null;
        }
        TenantOssDTO tenantOssDTO = new TenantOssDTO();
        BeanUtils.copyProperties(platformTenantOssDO, tenantOssDTO);
        return tenantOssDTO;
    }
}
