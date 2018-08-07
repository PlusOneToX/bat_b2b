package com.bat.order.service.order.convertor;

import java.util.Date;

import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.deliver.dto.data.OrderDeliveryDTO;
import com.bat.order.api.order.dto.common.OrderAddress;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;

public class OrderDeliveryConvertor {

    public static OrderDeliveryDO toOrderDeliveryDO(OrderAddress orderAddress, Integer orderId, Integer distributionId,
        String distributionName) {
        OrderDeliveryDO orderDeliveryDO = BeanUtils.copy(orderAddress, OrderDeliveryDO.class);
        orderDeliveryDO.setOrderId(orderId);
        orderDeliveryDO.setCreateTime(new Date());
        orderDeliveryDO.setDistributionId(distributionId);
        orderDeliveryDO.setDistributionName(distributionName);
        return orderDeliveryDO;
    }

    public static OrderDeliveryDTO toOrderDeliveryDTO(OrderDeliveryDO orderDeliveryDO) {
        if (orderDeliveryDO != null) {
            OrderDeliveryDTO dto = new OrderDeliveryDTO();
            org.springframework.beans.BeanUtils.copyProperties(orderDeliveryDO, dto);
            return dto;
        }
        return null;
    }
}
