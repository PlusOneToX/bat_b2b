package com.bat.order.api.order.dto.orderquery.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/2/12 13:51
 */
@Data
@ApiModel(description = "判断分销订单状态查询Qry")
public class AdminJudgeOrderQry {

    @ApiModelProperty(value = "订单id", example = "")
    private Integer id;

    @ApiModelProperty(value = "订单编号", example = "")
    private String orderNo;
}
