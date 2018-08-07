package com.bat.order.service.deliver.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.order.api.deliver.dto.data.OrderDeliveryTraceDTO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryTraceDO;

public class OrderDeliveryTraceConvertor {

    public static List<OrderDeliveryTraceDTO>
        toOrderDeliveryTraceDTOList(List<OrderDeliveryTraceDO> orderDeliveryTraceDOS, String logisticsStatus) {
        if (!CollectionUtils.isEmpty(orderDeliveryTraceDOS)) {
            return orderDeliveryTraceDOS.stream().map(orderDeliveryTraceDO -> {
                OrderDeliveryTraceDTO dto = new OrderDeliveryTraceDTO();
                BeanUtils.copyProperties(orderDeliveryTraceDO, dto);
                dto.setLogisticsStatus(logisticsStatus);
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
