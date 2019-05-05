package com.bat.dubboapi.order.cost.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.cost.dto.OrderCustomerCostRpcQryDTO;

public interface OrderCustomerCostServiceRpc {

     Response<OrderCustomerCostRpcQryDTO> getOrderCustomerCost(Integer orderId, Integer customerId);

}
