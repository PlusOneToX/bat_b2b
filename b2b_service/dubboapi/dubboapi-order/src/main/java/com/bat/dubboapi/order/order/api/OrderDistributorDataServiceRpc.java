package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;

import java.util.List;

public interface OrderDistributorDataServiceRpc {

    /**
     * 根据erp订单号查询订单归属分销商数据
     * @param orderErpNo
     * @param erpFlag 是否同步到ERP 1、是 0、否
     * @return
     */
    Response<List<OrderDistributorDataRpcDTO>> getByOrderErpNoAndErpFlag(String orderErpNo, Short erpFlag);

    /**
     * 根据订单id查询订单归属分销商数据
     * @param orderId
     * @param erpFlag 是否同步到ERP 1、是 0、否
     * @return
     */
    Response<List<OrderDistributorDataRpcDTO>> listByOrderIdAndErpFlag(Integer orderId,Short erpFlag);

    /**
     * 根据订单id和分销商id查询订单归属分销商数据
     * @param orderId
     * @param distributorId
     * @return
     */
    Response<OrderDistributorDataRpcDTO> getByOrderIdAndDistributorId(Integer orderId, Integer distributorId);
}
