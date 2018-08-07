package com.bat.order.api.cost;

import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

import java.util.List;

public interface OrderGoodsDistributorCostServiceI {
    void createList(List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList);

    /**
     * 校验订单是否是拼团订单
     * @param orderId
     * @return
     */
    Boolean checkIsGroup(Integer orderId);

    /**
     * 根据订单id和分销商id查询订单明细费用归属分销商的列表
     * @param orderId
     * @param distributorId
     * @return
     */
    List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(Integer orderId,Integer distributorId);
}
