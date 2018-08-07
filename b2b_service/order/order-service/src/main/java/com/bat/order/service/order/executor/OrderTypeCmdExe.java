package com.bat.order.service.order.executor;

import com.bat.order.service.common.error.OrderTypeErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.dao.order.OrderTypeDOMapper;
import com.bat.order.dao.order.dataobject.OrderTypeDO;
import com.bat.order.service.order.convertor.OrderTypeConvertor;

@Component
public class OrderTypeCmdExe {

    @Autowired
    private OrderTypeDOMapper orderTypeDOMapper;

    public void createOrderType(OrderTypeCmd qry) {
        OrderTypeDO aDo = OrderTypeConvertor.toOrderTypeDO(qry);
        orderTypeDOMapper.insert(aDo);
    }

    public void updateOrderType(OrderTypeCmd qry) {
        if (qry.getId() == null) {
            throw OrderException.buildException(OrderTypeErrorCode.B_ORDER_TYPE_ID_NULL);
        }
        OrderTypeDO aDo1 = orderTypeDOMapper.selectByPrimaryKey(qry.getId());
        if (aDo1 == null) {
            throw OrderException.buildException(OrderTypeErrorCode.B_ORDER_TYPE_ID_EXISTS);
        }
        OrderTypeDO aDo = OrderTypeConvertor.toOrderTypeDO(qry);
        orderTypeDOMapper.updateByPrimaryKeySelective(aDo);
    }

    public void deleteOrderType(Integer id) {
        OrderTypeDO aDo1 = orderTypeDOMapper.selectByPrimaryKey(id);
        if (aDo1 == null) {
            throw OrderException.buildException(OrderTypeErrorCode.B_ORDER_TYPE_ID_EXISTS);
        }
        orderTypeDOMapper.deleteByPrimaryKey(id);
    }
}
