package com.bat.platform.service.tenant;

import com.bat.platform.api.tenant.TenantDiyFactoryServiceI;
import com.bat.platform.api.tenant.dto.TenantDiyFactoryCmd;
import com.bat.platform.api.tenant.dto.data.TenantDiyFactoryDTO;
import com.bat.platform.service.tenant.executor.TenantDiyFactoryQryExe;
import com.bat.platform.service.tenant.executor.TenantDiyFactoryCmdExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 沙漠
 */
@Service
public class TenantDiyFactoryServiceImpl implements TenantDiyFactoryServiceI {

    @Resource
    private TenantDiyFactoryQryExe tenantDiyFactoryQryExe;

    @Resource
    private TenantDiyFactoryCmdExe tenantDiyFactoryCmdExe;


    @Override
    public List<TenantDiyFactoryDTO> listByTenantId(Integer tenantId) {
       return tenantDiyFactoryQryExe.listByTenantId(tenantId);
    }

    @Override
    public void add(TenantDiyFactoryCmd cmd) {
        tenantDiyFactoryCmdExe.add(cmd);
    }

    @Override
    public void deleteById(Integer id) {
        tenantDiyFactoryCmdExe.deleteById(id);
    }

    @Override
    public void update(TenantDiyFactoryCmd cmd) {
        tenantDiyFactoryCmdExe.update(cmd);
    }
}
