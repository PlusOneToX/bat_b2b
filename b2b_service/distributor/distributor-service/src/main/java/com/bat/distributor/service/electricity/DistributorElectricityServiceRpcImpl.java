package com.bat.distributor.service.electricity;


import com.bat.distributor.dao.electricity.dataobject.DistributorElectricityRelationMappingDO;
import com.bat.distributor.service.electricity.executor.DistributorElectricityRelationMappingQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.electricity.api.DistributorElectricityServiceRpc;
import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class DistributorElectricityServiceRpcImpl implements DistributorElectricityServiceRpc {

    @Autowired
    private DistributorElectricityRelationMappingQryExe distributorElectricityRelationMappingQryExe;

    @Override
    public Response<DistributorElectricityRelationMappingRpcDTO> getByDistributorIdAndSellerNick(Integer distributorId, String sellerNick) {
        DistributorElectricityRelationMappingDO relationMappingDO = distributorElectricityRelationMappingQryExe.getByDistributorIdAndSellerNick(distributorId,sellerNick);
        if(relationMappingDO==null){
            return Response.of(null);
        }
        DistributorElectricityRelationMappingRpcDTO relationMappingRpcDTO = new DistributorElectricityRelationMappingRpcDTO();
        BeanUtils.copyProperties(relationMappingDO,relationMappingRpcDTO);
        return Response.of(relationMappingRpcDTO);
    }

    @Override
    public Response<List<DistributorElectricityRelationMappingRpcDTO>> getAll() {
        List<DistributorElectricityRelationMappingDO> relationMappingDOS = distributorElectricityRelationMappingQryExe.getAll();
        List<DistributorElectricityRelationMappingRpcDTO> distributorElectricityRelationMappingRpcDTOS = new ArrayList<>();
        for (DistributorElectricityRelationMappingDO distributorElectricityRelationMappingDO : relationMappingDOS) {
            DistributorElectricityRelationMappingRpcDTO relationMappingRpcDTO = new DistributorElectricityRelationMappingRpcDTO();
            BeanUtils.copyProperties(distributorElectricityRelationMappingDO, relationMappingRpcDTO);
            distributorElectricityRelationMappingRpcDTOS.add(relationMappingRpcDTO);
        }
        return Response.of(distributorElectricityRelationMappingRpcDTOS);
    }
}
