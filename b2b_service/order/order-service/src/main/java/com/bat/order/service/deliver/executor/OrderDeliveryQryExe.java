package com.bat.order.service.deliver.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;

@Component
public class OrderDeliveryQryExe {

    @Autowired
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    public OrderDeliveryDO getByOrderId(Integer orderId) {
        return orderDeliveryDOMapper.getByOrderId(orderId);
    }
}
