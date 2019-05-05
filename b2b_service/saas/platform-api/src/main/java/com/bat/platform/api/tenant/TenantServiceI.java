package com.bat.platform.api.tenant;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.tenant.dto.TenantCmd;
import com.bat.platform.api.tenant.dto.TenantQry;
import com.bat.platform.api.tenant.dto.data.TenantDTO;
import com.bat.platform.api.tenant.dto.data.TenantSummaryDTO;

/**
 * 沙漠
 */
public interface TenantServiceI {
    /**
     * 查看租户列表
     * 
     * @param qry
     * @return
     */
    PageInfo<TenantDTO> list(TenantQry qry);

    void add(TenantCmd cmd);

    void update(TenantCmd cmd);

    void deleteById(Integer id);

    TenantDTO detail(Integer id);

    TenantSummaryDTO summary(Integer id);
}
