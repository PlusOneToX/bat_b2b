package com.bat.distributor.service.distributor.executor;

import com.bat.distributor.dao.distributor.DistributorExtendDataMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorExtendDataDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorExtendDataQryExe {

    @Autowired
    private DistributorExtendDataMapper distributorExtendDataMapper;

    public List<DistributorExtendDataDO> listByCondition() {
        return distributorExtendDataMapper.selectAll();
    }

    public DistributorExtendDataDO getByDistributorId(Integer distributorId) {
        return distributorExtendDataMapper.getByDistributorId(distributorId);
    }

    public List<DistributorExtendDataDO> getByDistributorIds(List<Integer> distributorIds) {
        List<DistributorExtendDataDO> list = distributorExtendDataMapper.listByDistributorIds(distributorIds);
        return list;
    }

    public List<DistributorExtendDataDO> listAvailableByErpNos(List<String> erpNos) {
        return distributorExtendDataMapper.listAvailableByErpNos(erpNos);
    }

}
