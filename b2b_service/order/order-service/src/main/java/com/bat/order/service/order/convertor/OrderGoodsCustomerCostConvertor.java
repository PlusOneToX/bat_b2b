package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.order.dto.orderquery.common.OrderGoodsCustomerCostDTO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;

public class OrderGoodsCustomerCostConvertor {

    public static List<OrderGoodsCustomerCostDO> toOrderGoodsCustomerCostList(Integer customerId,
        List<OrderGoodsDO> orderGoodsDOList) {
        if (customerId == null) {
            return null;
        }
        List<OrderGoodsCustomerCostDO> costDOList = new ArrayList<>();
        orderGoodsDOList.stream().forEach(orderGoodsDO -> {
            OrderGoodsCustomerCostDO costDO = new OrderGoodsCustomerCostDO();

        });

        return costDOList;
    }

    public static OrderGoodsCustomerCostDTO
        toOrderGoodsCustomerCostDTO(OrderGoodsCustomerCostDO orderGoodsCustomerCostDO) {
        if (orderGoodsCustomerCostDO != null) {
            OrderGoodsCustomerCostDTO dto = new OrderGoodsCustomerCostDTO();
            BeanUtils.copyProperties(orderGoodsCustomerCostDO, dto);
            return dto;
        }
        return null;
    }
}
