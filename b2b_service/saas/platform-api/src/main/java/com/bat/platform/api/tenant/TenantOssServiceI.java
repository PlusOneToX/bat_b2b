package com.bat.platform.api.tenant;

import com.bat.platform.api.tenant.dto.TenantOssCmd;
import com.bat.platform.api.tenant.dto.TenantOssQry;
import com.bat.platform.api.tenant.dto.data.TenantOssDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
public interface TenantOssServiceI {

    TenantOssDTO config(TenantOssQry qry);

    void add(TenantOssCmd cmd);

    void update(TenantOssCmd cmd);
}
