package com.bat.platform.api.tenant;

import com.bat.platform.api.tenant.dto.TenantSmsCmd;
import com.bat.platform.api.tenant.dto.TenantSmsQry;
import com.bat.platform.api.tenant.dto.data.TenantSmsDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantSmsServiceI {

    TenantSmsDTO config(TenantSmsQry qry);

    void add(TenantSmsCmd cmd);

    void update(TenantSmsCmd cmd);
}
