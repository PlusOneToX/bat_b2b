package com.bat.platform.service.tenant;

import com.bat.platform.api.tenant.TenantOssServiceI;
import com.bat.platform.api.tenant.dto.TenantOssCmd;
import com.bat.platform.api.tenant.dto.TenantOssQry;
import com.bat.platform.api.tenant.dto.data.TenantOssDTO;
import com.bat.platform.service.tenant.executor.TenantOssCmdExe;
import com.bat.platform.service.tenant.executor.TenantOssQryExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
@Service
public class TenantOssServiceImpl implements TenantOssServiceI {

    @Resource
    private TenantOssQryExe tenantOssQryExe;

    @Resource
    private TenantOssCmdExe tenantOssCmdExe;

    @Override
    public TenantOssDTO config(TenantOssQry qry) {
        TenantOssDTO tenantOssDTO = tenantOssQryExe.config(qry);
        return tenantOssDTO;
    }

    @Override
    public void add(TenantOssCmd cmd) {
        tenantOssCmdExe.add(cmd);
    }

    @Override
    public void update(TenantOssCmd cmd) {
        tenantOssCmdExe.update(cmd);
    }
}
