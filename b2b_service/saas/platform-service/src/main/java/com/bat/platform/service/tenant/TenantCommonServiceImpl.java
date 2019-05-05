package com.bat.platform.service.tenant;

import javax.annotation.Resource;

import com.bat.platform.service.tenant.executor.TenantCommonQryExe;
import org.springframework.stereotype.Service;

import com.bat.platform.api.tenant.TenantCommonServiceI;
import com.bat.platform.api.tenant.dto.TenantCommonCmd;
import com.bat.platform.api.tenant.dto.data.TenantCommonDTO;
import com.bat.platform.service.tenant.executor.TenantCommonCmdExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
@Service
public class TenantCommonServiceImpl implements TenantCommonServiceI {

    @Resource
    private TenantCommonQryExe tenantCommonQryExe;

    @Resource
    private TenantCommonCmdExe tenantCommonCmdExe;

    @Override
    public TenantCommonDTO config(Integer tenantId) {
        TenantCommonDTO tenantCommonDTO = tenantCommonQryExe.selectByTenantId(tenantId);
        return tenantCommonDTO;
    }

    @Override
    public void add(TenantCommonCmd cmd) {
        tenantCommonCmdExe.add(cmd);
    }

    @Override
    public void update(TenantCommonCmd cmd) {
        tenantCommonCmdExe.update(cmd);
    }
}
