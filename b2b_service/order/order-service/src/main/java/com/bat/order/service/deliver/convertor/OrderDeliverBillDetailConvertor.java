package com.bat.order.service.deliver.convertor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/7 17:00
 */
public class OrderDeliverBillDetailConvertor {
    public static List<OrderDeliverBillDetailDTO>
        toOrderDeliverBillDetailDTOList(List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOS) {
        return orderDeliverBillDetailDOS.stream().map(OrderDeliverBillDetailConvertor::toOrderDeliverBillDetailDTO)
            .collect(Collectors.toList());
    }

    public static OrderDeliverBillDetailDTO
        toOrderDeliverBillDetailDTO(OrderDeliverBillDetailDO orderDeliverBillDetailDO) {
        if (orderDeliverBillDetailDO != null) {
            OrderDeliverBillDetailDTO dto = new OrderDeliverBillDetailDTO();
            BeanUtils.copyProperties(orderDeliverBillDetailDO, dto);
            return dto;
        }
        return null;
    }
}
