package com.bat.distributor.service.electricity.executor;

import com.bat.distributor.dao.electricity.DistributorElectricityRelationMappingDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorElectricityRelationMappingCmdExe {

    @Autowired
    private DistributorElectricityRelationMappingDOMapper distributorElectricityRelationMappingDOMapper;
}
