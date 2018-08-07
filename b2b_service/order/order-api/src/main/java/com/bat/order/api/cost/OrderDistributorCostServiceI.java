package com.bat.order.api.cost;

import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;

import java.util.List;

public interface OrderDistributorCostServiceI {
    /**
     * 新增
     * @param orderDistributorCostDO
     */
    void create(OrderDistributorCostDO orderDistributorCostDO);



    /**
     * 跟据订单id和分销商Id查询订单归属分销商费用
     * @param orderId
     * @param distributorId
     * @return
     */
    OrderDistributorCostDO getByOrderIdAndDistributorId(Integer orderId, Integer distributorId);

    /**
     * 根据订单id获取直接下单的在线支付信息
     * @param orderId
     */
    List<OrderDistributorCostDO> getDirectPayInfoByOrderId(Integer orderId);
}
