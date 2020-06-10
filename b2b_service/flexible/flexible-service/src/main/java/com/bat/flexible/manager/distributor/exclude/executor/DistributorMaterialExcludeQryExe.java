package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorMaterialExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorMaterialExcludeQryExe {

    @Autowired
    private DistributorMaterialExcludeDOMapper distributorMaterialExcludeDOMapper;

    public List<DistributorMaterialExcludeDO> listByMaterialIdAndDelFlag(Integer materialId,Short delFlag) {
        return distributorMaterialExcludeDOMapper.listByMaterialIdAndDelFlag(materialId,delFlag);
    }

    public List<DistributorMaterialExcludeDO> listByDistributorIds(List<Integer> distributorIds) {
        return distributorMaterialExcludeDOMapper.listByDistributorIds(distributorIds);
    }
}
