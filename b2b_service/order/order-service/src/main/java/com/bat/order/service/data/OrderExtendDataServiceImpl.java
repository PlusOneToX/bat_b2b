package com.bat.order.service.data;

import com.bat.order.service.data.executor.OrderExtendDataCmdExe;
import com.bat.order.service.data.executor.OrderExtendDataQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.data.OrderExtendDataServiceI;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;

@Service
public class OrderExtendDataServiceImpl implements OrderExtendDataServiceI {

    @Autowired
    private OrderExtendDataCmdExe orderExtendDataCmdExe;

    @Autowired
    private OrderExtendDataQryExe orderExtendDataQryExe;

    @Override
    public OrderExtendDataDO getByOrderId(Integer orderId) {
        return orderExtendDataQryExe.getByOrderId(orderId);
    }

    @Override
    public OrderExtendDataDO getByOrderFactoryNo(String orderFactoryNo) {
        return orderExtendDataQryExe.getByOrderFactoryNo(orderFactoryNo);
    }

    @Override
    public OrderExtendDataDO getByOrderErpNo(String orderErpNo) {
        return orderExtendDataQryExe.getByOrderErpNo(orderErpNo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(OrderExtendDataDO orderExtendDataDO) {
        orderExtendDataCmdExe.update(orderExtendDataDO);
    }
}
