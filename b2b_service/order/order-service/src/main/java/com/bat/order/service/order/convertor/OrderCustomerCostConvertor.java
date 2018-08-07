package com.bat.order.service.order.convertor;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.order.dto.orderquery.common.OrderCustomerCostDTO;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/6 16:34
 */
public class OrderCustomerCostConvertor {
    public static OrderCustomerCostDTO toOrderCustomerCostDTO(OrderCustomerCostDO orderCustomerCostDO) {
        if (orderCustomerCostDO != null) {
            OrderCustomerCostDTO dto = new OrderCustomerCostDTO();
            BeanUtils.copyProperties(orderCustomerCostDO, dto);
            return dto;
        }
        return null;
    }
}
