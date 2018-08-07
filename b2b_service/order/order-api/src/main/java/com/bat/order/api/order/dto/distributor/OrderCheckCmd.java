package com.bat.order.api.order.dto.distributor;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销订单审核")
public class OrderCheckCmd {
    @NotNull(message = "P_ORDER_IDS_NULL")
    @ApiModelProperty(value = "ids列表", required = true)
    private List<Integer> ids;
    @NotNull(message = "P_ORDER_STATUS_NULL")
    @ApiModelProperty(value = "订单审核状态：2.已确认 3.已拒绝", required = true)
    private Short orderStatus;
}