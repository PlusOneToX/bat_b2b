package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorPictureExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorPictureExcludeQryExe {

    @Autowired
    private DistributorPictureExcludeDOMapper distributorPictureExcludeDOMapper;

    public List<DistributorPictureExcludeDO> listByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        return distributorPictureExcludeDOMapper.listByPictureIdAndDelFlag(pictureId,delFlag);
    }

    public List<DistributorPictureExcludeDO> listByDistributorId(List<Integer> distributorIds) {
        return distributorPictureExcludeDOMapper.listByDistributorId(distributorIds);
    }
}
