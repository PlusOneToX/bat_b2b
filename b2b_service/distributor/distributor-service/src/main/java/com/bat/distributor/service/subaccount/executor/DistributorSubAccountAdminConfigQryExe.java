package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.dao.subaccount.DistributorSubAccountAdminConfigDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountAdminConfigDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorSubAccountAdminConfigQryExe {

    @Autowired
    private DistributorSubAccountAdminConfigDOMapper distributorSubAccountAdminConfigDOMapper;


    public DistributorSubAccountAdminConfigDO getByDistributorId(Integer distributorId) {
        return distributorSubAccountAdminConfigDOMapper.getByDistributorId(distributorId);
    }
}
