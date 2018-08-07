package com.bat.order.api.order.dto.common;

import javax.validation.constraints.NotBlank;

import com.bat.order.api.basic.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单取消")
public class OrderCancelCmd extends BaseId {
    @NotBlank(message = "P_ORDER_CANCEL_REMARK_NULL")
    @ApiModelProperty(value = "订单取消理由", required = false, example = "0")
    private String remark;
}