package com.bat.dubboapi.distributor.distributor.api;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorMsgNeedDataDTO;

import java.util.List;

public interface DistributorExtendDataServiceRpc {


    Response<List<DistributorExtendDataRpcDTO>> listByCondition();

    Response<DistributorExtendDataRpcDTO> getByDistributorId(Integer distributorId);

    Response<List<DistributorMsgNeedDataDTO>> getByDistributorIds(List<Integer> distributorIds);

    Response<List<DistributorExtendDataRpcDTO>> listAvailableByErpNos(List<String> erpNos);
}
