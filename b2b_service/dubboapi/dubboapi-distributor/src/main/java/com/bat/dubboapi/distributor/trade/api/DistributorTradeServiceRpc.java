package com.bat.dubboapi.distributor.trade.api;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.trade.dto.DistributorTradeRpcQryDTO;

public interface DistributorTradeServiceRpc {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Response<DistributorTradeRpcQryDTO> getById(Integer id);

    /**
     * 根据分销商id查询结算方式
     * @param distributorId
     * @return
     */
    Response<DistributorTradeRpcQryDTO> getByDistributorId(Integer distributorId);
}
