package com.bat.distributor.service.electricity.executor;

import com.bat.distributor.dao.electricity.DistributorElectricityRelationMappingDOMapper;
import com.bat.distributor.dao.electricity.dataobject.DistributorElectricityRelationMappingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorElectricityRelationMappingQryExe {

    @Autowired
    private DistributorElectricityRelationMappingDOMapper distributorElectricityRelationMappingDOMapper;

    public DistributorElectricityRelationMappingDO getBySellerNick(String sellerNick) {
        return distributorElectricityRelationMappingDOMapper.getBySellerNick(sellerNick);
    }

    public DistributorElectricityRelationMappingDO getByDistributorIdAndSellerNick(Integer distributorId, String sellerNick) {
        return distributorElectricityRelationMappingDOMapper.getByDistributorIdAndSellerNick(distributorId,sellerNick);
    }

    public List<DistributorElectricityRelationMappingDO> getAll() {
        return distributorElectricityRelationMappingDOMapper.getAll();
    }
}
