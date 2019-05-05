package com.bat.platform.service.tenant;

import com.bat.platform.api.tenant.TenantErpServiceI;
import com.bat.platform.api.tenant.dto.TenantErpCmd;
import com.bat.platform.api.tenant.dto.data.TenantErpDTO;
import com.bat.platform.service.tenant.executor.TenantErpQryExe;
import com.bat.platform.service.tenant.executor.TenantErpCmdExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 沙漠
 */
@Service
public class TenantErpServiceImpl implements TenantErpServiceI {

    @Resource
    private TenantErpQryExe tenantErpQryExe;

    @Resource
    private TenantErpCmdExe tenantErpCmdExe;


    @Override
    public TenantErpDTO config(Integer id) {
        return tenantErpQryExe.selectByTenantId(id);
    }

    @Override
    public void add(TenantErpCmd cmd) {
        tenantErpCmdExe.add(cmd);
    }

    @Override
    public void update(TenantErpCmd cmd) {
        tenantErpCmdExe.update(cmd);
    }
}
