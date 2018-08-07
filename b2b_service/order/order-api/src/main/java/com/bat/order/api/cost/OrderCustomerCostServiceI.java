package com.bat.order.api.cost;

import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;

public interface OrderCustomerCostServiceI {
    /**
     * 新增
     * @param orderCustomerCostDO
     */
    void create(OrderCustomerCostDO orderCustomerCostDO);
}
