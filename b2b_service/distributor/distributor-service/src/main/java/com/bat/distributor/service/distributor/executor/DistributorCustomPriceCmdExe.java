package com.bat.distributor.service.distributor.executor;

import com.bat.distributor.dao.distributor.DistributorCustomPriceDOMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorCustomPriceCmdExe {

    @Autowired
    private DistributorCustomPriceDOMapper distributorCustomPriceDOMapper;

    public void update(DistributorCustomPriceDO distributorCustomPriceDO) {
        distributorCustomPriceDOMapper.updateByPrimaryKey(distributorCustomPriceDO);
    }

    public void create(DistributorCustomPriceDO customPriceDO) {
        distributorCustomPriceDOMapper.insert(customPriceDO);
    }
}
