package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.data.OrderCustomerDataRpcDTO;

public interface OrderCustomerDataServiceRpc {

    /**
     * 根据订单id查询归属C端客户信息
     * @param orderId
     * @return
     */
    Response<OrderCustomerDataRpcDTO> getByOrderId(Integer orderId);
}
