package com.bat.order.api.order.dto.common;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单类型id")
public class OrderTypeId {

    @NotNull(message = "P_ORDER_TYPE_ID_NULL")
    @ApiModelProperty("id")
    private Integer id;

}
