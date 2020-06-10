package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorPictureExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorPictureExcludeCmdExe {

    @Autowired
    private DistributorPictureExcludeDOMapper distributorPictureExcludeDOMapper;

    public void update(DistributorPictureExcludeDO distributorPictureExcludeDO) {
        distributorPictureExcludeDOMapper.updateByPrimaryKey(distributorPictureExcludeDO);
    }

    public void create(DistributorPictureExcludeDO excludeDO) {
        distributorPictureExcludeDOMapper.insert(excludeDO);
    }
}
