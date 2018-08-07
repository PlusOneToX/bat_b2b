package com.bat.order.service.order.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.order.api.cost.dto.data.OrderGoodsDistributorCostDTO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 23:54
 */
public class OrderGoodsDistributorCostConvertor {
    public static List<OrderGoodsDistributorCostDTO>
        toOrderGoodsDistributorCostDTOList(List<OrderGoodsDistributorCostDO> listByOrderGoodsId) {
        if (!CollectionUtils.isEmpty(listByOrderGoodsId)) {
            return listByOrderGoodsId.stream().map(OrderGoodsDistributorCostConvertor::toOrderGoodsDistributorCostDTO)
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static OrderGoodsDistributorCostDTO toOrderGoodsDistributorCostDTO(OrderGoodsDistributorCostDO costDO) {
        if (costDO != null) {
            OrderGoodsDistributorCostDTO dto = new OrderGoodsDistributorCostDTO();
            BeanUtils.copyProperties(costDO, dto);
            return dto;
        }
        return null;
    }
}
