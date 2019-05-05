package com.bat.dubboapi.distributor.electricity.api;

import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.distributor.common.Response;

import java.util.List;

public interface DistributorElectricityServiceRpc {

    Response<DistributorElectricityRelationMappingRpcDTO> getByDistributorIdAndSellerNick(Integer distributorId, String sellerNick);

    Response<List<DistributorElectricityRelationMappingRpcDTO>> getAll();
}
