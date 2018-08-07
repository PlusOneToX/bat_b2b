package com.bat.order.service.cost.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.cost.OrderGoodsCustomerCostMapper;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;

@Component
public class OrderGoodsCustomerCostCmdExe {

    @Autowired
    private OrderGoodsCustomerCostMapper orderGoodsCustomerCostDOMapper;

    public void createList(List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList) {
        orderGoodsCustomerCostDOList.stream().forEach(orderGoodsCustomerCostDO -> {
            orderGoodsCustomerCostDO.setCreateTime(new Date());
            orderGoodsCustomerCostDO.setUpdateTime(new Date());
            orderGoodsCustomerCostDOMapper.insert(orderGoodsCustomerCostDO);
        });
    }
}
