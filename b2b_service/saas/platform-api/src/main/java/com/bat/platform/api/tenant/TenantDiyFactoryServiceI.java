package com.bat.platform.api.tenant;

import com.bat.platform.api.tenant.dto.TenantDiyFactoryCmd;
import com.bat.platform.api.tenant.dto.data.TenantDiyFactoryDTO;

import java.util.List;

/**
 * 沙漠
 */
public interface TenantDiyFactoryServiceI {

    List<TenantDiyFactoryDTO> listByTenantId(Integer tenantId);

    void add(TenantDiyFactoryCmd cmd);

    void deleteById(Integer id);

    void update(TenantDiyFactoryCmd cmd);
}
