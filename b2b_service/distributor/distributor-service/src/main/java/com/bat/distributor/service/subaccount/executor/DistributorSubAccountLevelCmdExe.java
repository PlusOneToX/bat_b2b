package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.dao.subaccount.DistributorSubAccountLevelDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorSubAccountLevelCmdExe {

    @Autowired
    private DistributorSubAccountLevelDOMapper distributorSubAccountLevelDOMapper;

    public void update(DistributorSubAccountLevelDO accountLevelDO) {
        distributorSubAccountLevelDOMapper.updateByPrimaryKey(accountLevelDO);
    }

    public void create(DistributorSubAccountLevelDO levelDO) {
        distributorSubAccountLevelDOMapper.insert(levelDO);
    }
}
