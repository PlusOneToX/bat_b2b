package com.bat.order.service.order;

import java.util.List;

import com.bat.order.service.order.convertor.OrderTypeConvertor;
import com.bat.order.service.order.executor.OrderTypeCmdExe;
import com.bat.order.service.order.executor.OrderTypeQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.OrderTypeServiceI;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

@Service
public class OrderTypeServiceImpl implements OrderTypeServiceI {

    @Autowired
    private OrderTypeQryExe orderTypeQryExe;

    @Autowired
    private OrderTypeCmdExe orderTypeCmdExe;

    @Override
    public OrderTypeDO getById(Integer orderTypeId) {
        return orderTypeQryExe.getById(orderTypeId);
    }

    @Override
    public PageInfo<OrderTypeDTO> listOrderType(OrderTypeListQry qry) {
        return orderTypeQryExe.listOrderType(qry);
    }

    @Override
    public void createOrderType(OrderTypeCmd qry) {
        orderTypeCmdExe.createOrderType(qry);
    }

    @Override
    public void updateOrderType(OrderTypeCmd qry) {
        orderTypeCmdExe.updateOrderType(qry);
    }

    @Override
    public void deleteOrderType(Integer id) {
        orderTypeCmdExe.deleteOrderType(id);
    }

    @Override
    public OrderTypeDTO getOrderType(Integer id) {
        return OrderTypeConvertor.toOrderTypeDTO(orderTypeQryExe.getById(id));
    }

    @Override
    public List<OrderTypeDO> listByCondition(Short openFlag) {
        return orderTypeQryExe.listByCondition(openFlag);
    }
}
