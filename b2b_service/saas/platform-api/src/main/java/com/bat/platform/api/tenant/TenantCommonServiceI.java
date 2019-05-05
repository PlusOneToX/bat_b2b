package com.bat.platform.api.tenant;

import com.bat.platform.api.tenant.dto.TenantCommonCmd;
import com.bat.platform.api.tenant.dto.data.TenantCommonDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantCommonServiceI {

    TenantCommonDTO config(Integer tenantId);

    void add(TenantCommonCmd cmd);

    void update(TenantCommonCmd cmd);
}
