package com.bat.financial.api.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 9:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "QueryRefundCmd", description = "查询退款")
public class QueryRefundCmd extends BaseTrade {
    @ApiModelProperty(value = "商户订单号", example = "bat20187")
    private String outTradeNo;
    @ApiModelProperty(value = "商户退款单号", example = "bat20187")
    private String outRefundNo;
}
