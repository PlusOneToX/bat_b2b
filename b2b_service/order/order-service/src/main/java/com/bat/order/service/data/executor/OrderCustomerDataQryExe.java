package com.bat.order.service.data.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;

@Component
public class OrderCustomerDataQryExe {

    @Autowired
    private OrderCustomerDataMapper orderCustomerDataMapper;

    /**
     * 根据订单id和C端客户id获取C端客户层数据
     *
     * @param orderId
     * @param customerId
     * @return
     */
    public OrderCustomerDataDO getOrderCustomerData(Integer orderId, Integer customerId) {
        OrderCustomerDataDO orderCustomerDataDO =
            orderCustomerDataMapper.getByOrderIdAndCustomerId(orderId, customerId);
        return orderCustomerDataDO;
    }

    public OrderCustomerDataDO getOrderCustomerData(Integer orderId) {
        return orderCustomerDataMapper.getByOrderId(orderId);
    }
}
