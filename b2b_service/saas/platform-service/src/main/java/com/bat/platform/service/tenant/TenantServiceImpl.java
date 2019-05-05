package com.bat.platform.service.tenant;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.platform.api.tenant.TenantServiceI;
import com.bat.platform.api.tenant.dto.TenantCmd;
import com.bat.platform.api.tenant.dto.TenantQry;
import com.bat.platform.api.tenant.dto.data.TenantDTO;
import com.bat.platform.api.tenant.dto.data.TenantSummaryDTO;
import com.bat.platform.service.tenant.executor.TenantCmdExe;
import com.bat.platform.service.tenant.executor.TenantQryExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 沙漠
 */
@Service
public class TenantServiceImpl implements TenantServiceI {

    @Resource
    private TenantCmdExe cmdExe;
    @Resource
    private TenantQryExe qryExe;

    @Override
    public PageInfo<TenantDTO> list(TenantQry qry) {
        PageHelper.startPage(qry.getPage(),qry.getSize());
        return qryExe.list(qry);
    }

    @Override
    public void add(TenantCmd cmd) {
        cmdExe.add(cmd);
    }

    @Override
    public void update(TenantCmd cmd) {
        cmdExe.update(cmd);
    }

    @Override
    public void deleteById(Integer id) {
        cmdExe.deleteById(id);
    }

    @Override
    public TenantDTO detail(Integer id) {
        return qryExe.detail(id);
    }

    @Override
    public TenantSummaryDTO summary(Integer id) {
        TenantSummaryDTO tenantSummaryDTO = qryExe.summary(id);
        return tenantSummaryDTO;
    }
}
