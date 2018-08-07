package com.bat.order.service.deliver.convertor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.deliver.dto.data.OrderDeliverBillDTO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/7 17:00
 */
public class OrderDeliverBillConvertor {
    public static List<OrderDeliverBillDTO> toOrderDeliverBillDTOList(List<OrderDeliverBillDO> orderDeliverBillDOS) {
        return orderDeliverBillDOS.stream().map(OrderDeliverBillConvertor::toOrderDeliverBillDTO)
            .collect(Collectors.toList());
    }

    public static OrderDeliverBillDTO toOrderDeliverBillDTO(OrderDeliverBillDO orderDeliverBillDO) {
        if (orderDeliverBillDO != null) {
            OrderDeliverBillDTO dto = new OrderDeliverBillDTO();
            BeanUtils.copyProperties(orderDeliverBillDO, dto);
            return dto;
        }
        return null;
    }
}
