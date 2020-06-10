package com.bat.flexible.manager.distributor.cooperation.executor;

import com.bat.flexible.dao.distributor.FlexibleDistributorCooperationDOMapper;
import com.bat.flexible.dao.distributor.dataobject.FlexibleDistributorCooperationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlexibleDistributorCooperationCmdExe {

    @Autowired
    private FlexibleDistributorCooperationDOMapper flexibleDistributorCooperationDOMapper;


    public void create(FlexibleDistributorCooperationDO flexibleDistributorCooperationDO) {
        flexibleDistributorCooperationDOMapper.insert(flexibleDistributorCooperationDO);
    }

    public void update(FlexibleDistributorCooperationDO cooperationDO) {
        flexibleDistributorCooperationDOMapper.updateByPrimaryKey(cooperationDO);
    }
}
