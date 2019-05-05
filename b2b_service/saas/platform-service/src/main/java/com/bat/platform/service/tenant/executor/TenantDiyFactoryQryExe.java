package com.bat.platform.service.tenant.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.platform.api.tenant.dto.data.TenantDiyFactoryDTO;
import com.bat.platform.dao.tenant.PlatformTenantDiyFactoryMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantDiyFactoryDO;

/**
 * 沙漠
 */
@Component
public class TenantDiyFactoryQryExe {

    @Resource
    private PlatformTenantDiyFactoryMapper platformTenantDiyFactoryMapper;

    public List<TenantDiyFactoryDTO> listByTenantId(Integer tenantId) {
        List<PlatformTenantDiyFactoryDO> platformTenantDiyFactoryDOS =
            platformTenantDiyFactoryMapper.listByTenantId(tenantId);
        List<TenantDiyFactoryDTO> list = new ArrayList<>();
        for (PlatformTenantDiyFactoryDO platformTenantDiyFactoryDO : platformTenantDiyFactoryDOS) {
            TenantDiyFactoryDTO tenantDiyFactoryDTO = new TenantDiyFactoryDTO();
            BeanUtils.copyProperties(platformTenantDiyFactoryDO, tenantDiyFactoryDTO);
            list.add(tenantDiyFactoryDTO);
        }
        return list;
    }

    public PlatformTenantDiyFactoryRpcDTO diyFactoryConfig(Integer tenantId, String factoryNo) {
        PlatformTenantDiyFactoryDO platformTenantDiyFactoryDO =
            platformTenantDiyFactoryMapper.selectByTenantIdAndFactoryNo(tenantId, factoryNo);
        if (platformTenantDiyFactoryDO == null) {
            return null;
        }
        PlatformTenantDiyFactoryRpcDTO platformTenantDiyFactoryRpcDTO = new PlatformTenantDiyFactoryRpcDTO();
        BeanUtils.copyProperties(platformTenantDiyFactoryDO, platformTenantDiyFactoryRpcDTO);
        return platformTenantDiyFactoryRpcDTO;
    }

    public List<String> factoryNoList() {
        return platformTenantDiyFactoryMapper.factoryNoList();
    }
}
