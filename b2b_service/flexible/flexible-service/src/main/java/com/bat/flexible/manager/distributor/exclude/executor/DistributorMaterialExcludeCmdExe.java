package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorMaterialExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorMaterialExcludeCmdExe {

    @Autowired
    private DistributorMaterialExcludeDOMapper distributorMaterialExcludeDOMapper;

    public void create(DistributorMaterialExcludeDO excludeDO) {
        distributorMaterialExcludeDOMapper.insert(excludeDO);
    }

    public void update(DistributorMaterialExcludeDO distributorMaterialExcludeDO) {
        distributorMaterialExcludeDOMapper.updateByPrimaryKey(distributorMaterialExcludeDO);
    }
}
