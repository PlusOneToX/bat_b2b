package com.bat.dubboapi.order.exchange.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.exchange.dto.OrderGoodsExchangeCodeRpcQryDTO;

import java.util.List;

public interface OrderGoodsExchangeCodeServiceRpc {

    /**
     * 根据订单查询兑换码列表
     * @param orderId
     * @return
     */
    Response<List<OrderGoodsExchangeCodeRpcQryDTO>> listByOrderId(Integer orderId);
}
