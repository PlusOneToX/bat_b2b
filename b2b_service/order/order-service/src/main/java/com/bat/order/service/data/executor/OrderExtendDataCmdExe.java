package com.bat.order.service.data.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;

@Component
public class OrderExtendDataCmdExe {

    @Autowired
    private OrderExtendDataMapper orderExtendDataMapper;

    public void update(OrderExtendDataDO orderExtendDataDO) {
        orderExtendDataMapper.updateByPrimaryKey(orderExtendDataDO);
    }
}
