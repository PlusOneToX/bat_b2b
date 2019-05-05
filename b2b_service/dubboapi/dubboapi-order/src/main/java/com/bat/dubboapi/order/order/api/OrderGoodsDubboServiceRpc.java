package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDetailRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;

import java.util.List;

public interface OrderGoodsDubboServiceRpc {
    /**
     * 根据订单id查询列表
     * @param orderId
     * @return
     */
    Response<List<OrderGoodsRpcDTO>> listOrderGoodsByOrderId(Integer orderId);

    /**
     * 根据订单明细查看商品详情
     * @param orderId
     * @return
     */
    Response<List<OrderGoodsDetailRpcDTO>> listOrderGoodsDetailByOrderId(Integer orderId);
}
