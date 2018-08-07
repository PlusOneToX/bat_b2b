package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.type.dto.OrderTypeRpcQryDTO;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/28 17:29
 */
public class OrderTypeConvertor {

    public static List<OrderTypeDTO> toOrderTypeDTO(List<OrderTypeDO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(orderTypeDO -> {
                OrderTypeDTO dto = new OrderTypeDTO();
                BeanUtils.copyProperties(orderTypeDO, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static OrderTypeDO toOrderTypeDO(OrderTypeCmd qry) {
        OrderTypeDO aDo = new OrderTypeDO();
        BeanUtils.copyProperties(qry, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static OrderTypeDTO toOrderTypeDTO(OrderTypeDO byId) {
        if (byId != null) {
            OrderTypeDTO dto = new OrderTypeDTO();
            BeanUtils.copyProperties(byId, dto);
            return dto;
        }
        return null;
    }

    public static OrderTypeRpcQryDTO toOrderTypeRpcQryDTO(OrderTypeDO orderTypeDO) {
        if (orderTypeDO == null) {
            return null;
        }
        OrderTypeRpcQryDTO orderTypeRpcQryDTO = new OrderTypeRpcQryDTO();
        BeanUtils.copyProperties(orderTypeDO, orderTypeRpcQryDTO);
        return orderTypeRpcQryDTO;
    }
}
