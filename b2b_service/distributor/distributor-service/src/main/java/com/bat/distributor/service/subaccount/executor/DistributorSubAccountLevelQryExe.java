package com.bat.distributor.service.subaccount.executor;

import java.util.List;

import com.bat.distributor.dao.subaccount.DistributorSubAccountLevelDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorSubAccountLevelQryExe {

    @Autowired
    private DistributorSubAccountLevelDOMapper distributorSubAccountLevelDOMapper;

    public List<DistributorSubAccountLevelDO> listByDistributorId(Integer distributorId) {
        return distributorSubAccountLevelDOMapper.listByDistributorId(distributorId);
    }

    public DistributorSubAccountLevelDO findParentLevel(Integer sonLevelId) {
        return distributorSubAccountLevelDOMapper.findParentLevel(sonLevelId);
    }

    public DistributorSubAccountLevelDO getById(Integer levelId) {
        return distributorSubAccountLevelDOMapper.selectByPrimaryKey(levelId);
    }
}
