package com.bat.order.service.data.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.co.OrderExtendDataCO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;

@Component
public class OrderExtendDataQryExe {

    @Autowired
    private OrderExtendDataMapper orderExtendDataMapper;

    public OrderExtendDataDO getByOrderId(Integer orderId) {
        return orderExtendDataMapper.getByOrderId(orderId);
    }

    public OrderExtendDataDO getByOrderFactoryNo(String orderFactoryNo) {
        return orderExtendDataMapper.getByOrderFactoryNo(orderFactoryNo);
    }

    public List<OrderExtendDataCO> listExtendDataSimpleByOrderIdList(List<Integer> orderIdList) {
        return orderExtendDataMapper.listExtendDataSimpleByOrderIdList(orderIdList);
    }

    public OrderExtendDataDO getByOrderErpNo(String orderErpNo) {
        return orderExtendDataMapper.getByOrderErpNo(orderErpNo);
    }

    public OrderExtendDataCO getExtendDataSimpleByOrderId(Integer orderId) {
        return orderExtendDataMapper.getExtendDataSimpleByOrderId(orderId);
    }

    /**
     * <h2>根据bat订单id查询工厂订单编号</h2>
     * 
     * @param orderId
     */
    public String queryOrderFactoryNoByOrderId(Integer orderId) {
        return orderExtendDataMapper.queryOrderFactoryNoByOrderId(orderId);
    }
}
