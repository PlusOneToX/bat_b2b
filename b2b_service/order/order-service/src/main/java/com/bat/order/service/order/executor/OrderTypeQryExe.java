package com.bat.order.service.order.executor;

import java.util.List;

import com.bat.order.service.order.convertor.OrderTypeConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.order.OrderTypeDOMapper;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

@Component
public class OrderTypeQryExe {

    @Autowired
    private OrderTypeDOMapper orderTypeDOMapper;

    public OrderTypeDO getById(Integer id) {
        return orderTypeDOMapper.selectByPrimaryKey(id);
    }

    public PageInfo<OrderTypeDTO> listOrderType(OrderTypeListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<OrderTypeDO> orderTypeDOS = orderTypeDOMapper.selectByParams(qry.getName(), qry.getErpType());
        PageInfo pageInfo = new PageInfo(orderTypeDOS);
        List<OrderTypeDTO> list = OrderTypeConvertor.toOrderTypeDTO(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<OrderTypeDO> listByCondition(Short openFlag) {
        return orderTypeDOMapper.listByCondition(openFlag);
    }

    public List<OrderTypeDO> listBySpecialFlag(Short specialFlag) {
        return orderTypeDOMapper.listBySpecialFlag(specialFlag);
    }

}
