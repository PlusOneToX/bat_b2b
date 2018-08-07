package com.bat.order.api.order.dto.orderquery.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/14 22:23
 */
@Data
public class UserPCDistributorOrderDeliverBillDetailQry {
    @NotNull(message = "P_ORDER_DELIVER_BILL_ID_NULL")
    @ApiModelProperty(value = "订单发货单id", required = true, example = "1")
    private Integer orderDeliverBillId;
}
