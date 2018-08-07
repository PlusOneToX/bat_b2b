package com.bat.order.api.order.dto.orderquery.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/2/12 13:51
 */
@Data
@ApiModel(description = "判断分销订单状态查询Qry")
public class AdminJudgeOrderDTO {

    @ApiModelProperty(value = "C端订单标志", example = "")
    private Boolean customerOrderFlag = false;

    @ApiModelProperty(value = "分销层订单标志", example = "")
    private Boolean distributorOrderFlag = false;

    @ApiModelProperty(value = "ERP同步订单标志", example = "")
    private Boolean erpOrderFlag = false;
}
