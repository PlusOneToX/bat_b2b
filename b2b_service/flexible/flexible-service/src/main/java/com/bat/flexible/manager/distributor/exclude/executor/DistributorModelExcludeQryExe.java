package com.bat.flexible.manager.distributor.exclude.executor;

import com.bat.flexible.dao.distributor.DistributorModelExcludeDOMapper;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorModelExcludeQryExe {

    @Autowired
    private DistributorModelExcludeDOMapper distributorModelExcludeDOMapper;


    public List<DistributorModelExcludeDO> listByModelId(Integer modelId) {
        return distributorModelExcludeDOMapper.listByModelId(modelId);
    }

    public List<DistributorModelExcludeDO> listByDistributorIds(List<Integer> distributorIds) {
        return distributorModelExcludeDOMapper.listByDistributorIds(distributorIds);
    }
}
