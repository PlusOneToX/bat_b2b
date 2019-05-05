package com.bat.platform.service.tenant.executor;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.platform.api.tenant.dto.data.TenantErpDTO;
import com.bat.platform.dao.tenant.PlatformTenantErpMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantErpDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/4 16:01
 */
@Component
public class TenantErpQryExe {

    @Resource
    private PlatformTenantErpMapper platformTenantErpMapper;

    public TenantErpDTO  selectByTenantId(Integer tenantId) {
        PlatformTenantErpDO tenantErpDO = platformTenantErpMapper.selectByTenantId(tenantId);
        if (tenantErpDO == null) {
            return null;
        }
        TenantErpDTO tenantErpDTO = new TenantErpDTO();
        BeanUtils.copyProperties(tenantErpDO, tenantErpDTO);
        return tenantErpDTO;
    }

    public PlatformTenantErpRpcDTO erpConfig(Integer tenantId, Short erpType) {
        PlatformTenantErpDO platformTenantErpDO = platformTenantErpMapper.selectByTenantIdAndErpType(tenantId, erpType);
        if (platformTenantErpDO == null) {
            return null;
        }
        PlatformTenantErpRpcDTO platformTenantErpRpcDTO = new PlatformTenantErpRpcDTO();
        BeanUtils.copyProperties(platformTenantErpDO, platformTenantErpRpcDTO);
        return platformTenantErpRpcDTO;
    }
}
