package com.bat.dubboapi.order.cost.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.cost.dto.OrderDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.cost.dto.OrderGoodsDistributorCostRpcQryDTO;

import java.util.List;

public interface OrderDistributorCostServiceRpc {

    /**
     * 根据订单id和分销商id查询订单归属分销商的费用
     * @param orderId
     * @param distributorId
     * @return
     */
    Response<OrderDistributorCostRpcQryDTO> getByOrderIdAndDistributorId(Integer orderId, Integer distributorId);

    /**
     * 根据订单id和分销商id查询订单明细归属分销商的费用
     * @param orderId
     * @param distributorId
     * @return
     */
    Response<List<OrderGoodsDistributorCostRpcQryDTO>> listGoodsCostByOrderIdAndDistributorId(Integer orderId, Integer distributorId);

    /**
     * 根据订单id判断是否是拼团
     * @param orderId
     * @return
     */
    Response<Boolean> checkIsGroup(Integer orderId);

    /**
     * 根据订单id获取直接下单的在线支付信息
     */
    Response<List<OrderDistributorCostRpcQryDTO>> getDirectPayInfoByOrderId(Integer orderId);

}
