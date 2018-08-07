package com.bat.order.service.deliver;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.deliver.OrderDeliveryServiceI;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.service.deliver.executor.OrderDeliveryCmdExe;
import com.bat.order.service.deliver.executor.OrderDeliveryQryExe;

@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryServiceI {

    @Autowired
    private OrderDeliveryCmdExe orderDeliveryCmdExe;

    @Autowired
    private OrderDeliveryQryExe orderDeliveryQryExe;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(OrderDeliveryDO orderDeliveryDO) {
        orderDeliveryDO.setCreateTime(new Date());
        orderDeliveryDO.setUpdateTime(new Date());
        orderDeliveryCmdExe.create(orderDeliveryDO);
    }

    @Override
    public OrderDeliveryDO getByOrderId(Integer orderId) {
        return orderDeliveryQryExe.getByOrderId(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(OrderDeliveryDO orderDeliveryDO) {
        orderDeliveryCmdExe.update(orderDeliveryDO);
    }

}
