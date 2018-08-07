package com.bat.order.service.order.convertor;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.order.dto.orderquery.common.OrderCustomerDataDTO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/6 16:34
 */
public class OrderCustomerDataConvertor {
    public static OrderCustomerDataDTO toOrderCustomerDataDTO(OrderCustomerDataDO orderCustomerDataDO) {
        if (orderCustomerDataDO != null) {
            OrderCustomerDataDTO dto = new OrderCustomerDataDTO();
            BeanUtils.copyProperties(orderCustomerDataDO, dto);
            return dto;
        }
        return null;
    }
}
