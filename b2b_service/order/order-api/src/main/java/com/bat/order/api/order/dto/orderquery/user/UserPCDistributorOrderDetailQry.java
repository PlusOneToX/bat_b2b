package com.bat.order.api.order.dto.orderquery.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 前台订单详情查询
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单详情")
public class UserPCDistributorOrderDetailQry {

    @ApiModelProperty(value = "B端客户id", example = "1")
    private Integer distributorId;

    @ApiModelProperty("订单主键")
    private Integer orderId;

}
