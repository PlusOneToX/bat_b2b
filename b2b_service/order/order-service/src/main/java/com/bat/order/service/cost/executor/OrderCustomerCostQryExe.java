package com.bat.order.service.cost.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;

@Component
public class OrderCustomerCostQryExe {

    @Autowired
    private OrderCustomerCostMapper orderCustomerCostMapper;

    public OrderCustomerCostDO getOrderCustomerCost(Integer orderId, Integer customerId) {
        return orderCustomerCostMapper.getByOrderIdAndCustomerId(orderId, customerId);
    }

}
