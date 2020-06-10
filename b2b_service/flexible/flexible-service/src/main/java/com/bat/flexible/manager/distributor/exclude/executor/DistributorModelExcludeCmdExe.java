package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorModelExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorModelExcludeCmdExe {

    @Autowired
    private DistributorModelExcludeDOMapper distributorModelExcludeDOMapper;


    public void create(DistributorModelExcludeDO excludeDO) {
        distributorModelExcludeDOMapper.insert(excludeDO);
    }

    public void deleteById(Integer id) {
        distributorModelExcludeDOMapper.deleteByPrimaryKey(id);
    }
}
