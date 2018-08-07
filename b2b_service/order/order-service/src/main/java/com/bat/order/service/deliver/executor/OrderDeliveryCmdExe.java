package com.bat.order.service.deliver.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;

@Component
public class OrderDeliveryCmdExe {

    @Autowired
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    public void create(OrderDeliveryDO orderDeliveryDO) {
        orderDeliveryDOMapper.insert(orderDeliveryDO);
    }

    public void update(OrderDeliveryDO orderDeliveryDO) {
        orderDeliveryDOMapper.updateByPrimaryKey(orderDeliveryDO);
    }

}
