package com.bat.distributor.service.electricity;


import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.electricity.DistributorElectricityRelationMappingServiceI;
import com.bat.distributor.dao.electricity.dataobject.DistributorElectricityRelationMappingDO;
import com.bat.distributor.service.electricity.executor.DistributorElectricityRelationMappingQryExe;
import com.bat.distributor.service.common.DistributorElectricityErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributorElectricityRelationMappingServiceImpl implements DistributorElectricityRelationMappingServiceI {


    @Autowired
    private DistributorElectricityRelationMappingQryExe distributorElectricityRelationMappingQryExe;

    @Override
    public Response getBySellerNick(String sellerNick) {
        DistributorElectricityRelationMappingDO relationMappingDO  = distributorElectricityRelationMappingQryExe.getBySellerNick(sellerNick);
        if(relationMappingDO ==null){
            throw DistributorException.buildException(DistributorElectricityErrorCode.D_DISTRIBUTOR_ELECTRICITY_NOT_YET_CONFIG);
        }
        return Response.of(relationMappingDO);
    }
}
