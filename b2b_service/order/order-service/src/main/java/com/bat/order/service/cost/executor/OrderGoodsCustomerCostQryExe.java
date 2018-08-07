package com.bat.order.service.cost.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;

@Component
public class OrderGoodsCustomerCostQryExe {

    @Autowired
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostMapper;

    public List<OrderGoodsCustomerCostDO> listOrderGoodsCustomerCost(Integer orderId, Integer customerId) {
        return orderGoodsCustomerCostMapper.listByOrderIdAndCustomerId(orderId, customerId);
    }

}
