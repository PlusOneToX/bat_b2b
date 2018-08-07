package com.bat.order.api.order.dto.orderquery.common;

import com.bat.order.api.cost.dto.data.OrderDistributorCostDTO;
import com.bat.order.api.data.dto.data.OrderDistributorDataDTO;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 默认分销商信息 订单所有等级分销商信息
 * @date: 2018/6/24 23:25
 */
@Data
public class OrderDistributorDTO implements Cloneable {
    /**
     * 订单的默认分销商数据信息
     */
    private OrderDistributorDataDTO orderDistributorDataDTO;
    /**
     * 订单的默认分销商消费信息
     */
    private OrderDistributorCostDTO orderDistributorCostDTO;

    @SneakyThrows
    @Override
    public OrderDistributorDTO clone() {
        OrderDistributorDTO clone = (OrderDistributorDTO)super.clone();
        clone.setOrderDistributorCostDTO(this.orderDistributorCostDTO.clone());
        clone.setOrderDistributorDataDTO(this.orderDistributorDataDTO.clone());
        return clone;
    }
}
