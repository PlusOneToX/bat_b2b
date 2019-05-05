package com.bat.platform.api.tenant;

import java.util.List;

import com.bat.platform.api.tenant.dto.TenantUrlCmd;
import com.bat.platform.api.tenant.dto.TenantUrlQry;
import com.bat.platform.api.tenant.dto.data.TenantUrlDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantUrlServiceI {

    List<TenantUrlDTO> listByTenantId(Integer tenantId);

    void add(TenantUrlCmd cmd);

    void update(TenantUrlCmd cmd);

    void deleteById(Integer id);

    /**
     * 获取租户地址
     * 
     * @param qry
     * @return
     */
    TenantUrlDTO get(TenantUrlQry qry);

}
