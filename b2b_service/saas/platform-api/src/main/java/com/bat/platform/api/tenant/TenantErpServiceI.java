package com.bat.platform.api.tenant;

import com.bat.platform.api.tenant.dto.TenantErpCmd;
import com.bat.platform.api.tenant.dto.data.TenantErpDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantErpServiceI {
    TenantErpDTO config(Integer id);

    void add(TenantErpCmd cmd);

    void update(TenantErpCmd cmd);
}
