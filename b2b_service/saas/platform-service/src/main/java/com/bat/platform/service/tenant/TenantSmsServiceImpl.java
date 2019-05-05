package com.bat.platform.service.tenant;

import com.bat.platform.api.tenant.TenantSmsServiceI;
import com.bat.platform.api.tenant.dto.TenantSmsCmd;
import com.bat.platform.api.tenant.dto.TenantSmsQry;
import com.bat.platform.api.tenant.dto.data.TenantSmsDTO;
import com.bat.platform.service.tenant.executor.TenantSmsQryExe;
import com.bat.platform.service.tenant.executor.TenantSmsCmdExe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
@Service
public class TenantSmsServiceImpl implements TenantSmsServiceI {

    @Resource
    private TenantSmsQryExe tenantSmsQryExe;

    @Resource
    private TenantSmsCmdExe tenantSmsCmdExe;


    @Override
    public TenantSmsDTO config(TenantSmsQry qry) {
        TenantSmsDTO tenantSmsDTO =tenantSmsQryExe.config(qry);
        return tenantSmsDTO;
    }

    @Transactional
    @Override
    public void add(TenantSmsCmd cmd) {
        tenantSmsCmdExe.add(cmd);
    }

    @Transactional
    @Override
    public void update(TenantSmsCmd cmd) {
        tenantSmsCmdExe.update(cmd);
    }
}
