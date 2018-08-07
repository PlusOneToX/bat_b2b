package com.bat.order.api.order.dto.orderquery.common;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 默认分销商信息 订单所有等级分销商信息
 * @date: 2018/6/24 23:25
 */
@Data
public class OrderCustomerDTO implements Cloneable {

    private OrderCustomerDataDTO orderDistributorDataDTO;

    private OrderCustomerCostDTO orderDistributorCostDTO;

    @SneakyThrows
    @Override
    public OrderCustomerDTO clone() {
        OrderCustomerDTO clone = (OrderCustomerDTO)super.clone();
        clone.setOrderDistributorCostDTO(this.orderDistributorCostDTO.clone());
        clone.setOrderDistributorDataDTO(this.orderDistributorDataDTO.clone());
        return clone;
    }

}
