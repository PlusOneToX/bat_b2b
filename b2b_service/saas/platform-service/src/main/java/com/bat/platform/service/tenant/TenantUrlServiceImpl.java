package com.bat.platform.service.tenant;

import java.util.List;

import javax.annotation.Resource;

import com.bat.platform.service.tenant.executor.TenantUrlCmdExe;
import com.bat.platform.service.tenant.executor.TenantUrlQryExe;
import org.springframework.stereotype.Service;

import com.bat.platform.api.tenant.TenantUrlServiceI;
import com.bat.platform.api.tenant.dto.TenantUrlCmd;
import com.bat.platform.api.tenant.dto.TenantUrlQry;
import com.bat.platform.api.tenant.dto.data.TenantUrlDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 18:49
 */
@Service
public class TenantUrlServiceImpl implements TenantUrlServiceI {

    @Resource
    private TenantUrlQryExe tenantUrlQryExe;

    @Resource
    private TenantUrlCmdExe tenantUrlCmdExe;

    @Override
    public List<TenantUrlDTO> listByTenantId(Integer tenantId) {
        List<TenantUrlDTO> list = tenantUrlQryExe.listByTenantId(tenantId);
        return list;
    }

    @Override
    public void add(TenantUrlCmd cmd) {
        tenantUrlCmdExe.add(cmd);
    }

    @Override
    public void update(TenantUrlCmd cmd) {
        tenantUrlCmdExe.update(cmd);
    }

    @Override
    public void deleteById(Integer id) {
        tenantUrlCmdExe.deleteById(id);
    }

    @Override
    public TenantUrlDTO get(TenantUrlQry qry) {
        return tenantUrlQryExe.get(qry);
    }
}
