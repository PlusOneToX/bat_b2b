package com.bat.order.api.cost;

import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;

import java.util.List;

public interface OrderGoodsCustomerCostServiceI {
    /**
     * 批量添加
     * @param orderGoodsCustomerCostDOList
     */
    void createList(List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList);
}
