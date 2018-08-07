package com.bat.financial.api.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 9:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "RefundTradeCmd", description = "退款")
public class RefundTradeCmd extends BaseTrade {

    @ApiModelProperty(value = "订单id 一个订单号", example = "100000")
    private String orderId;

    @ApiModelProperty(value = "商户订单号", example = "bat20187")
    private String outTradeNo;

    @ApiModelProperty(value = "客户id（可以是C端客户id,也可以是B端客户Id）", required = true, example = "100000")
    private Integer customerId;

    @ApiModelProperty(value = "退款金额", required = false, example = "0.01")
    private Double refundAmount;

    @ApiModelProperty(value = "原订单金额", required = false, example = "0.01")
    private Double totalAmount;

    @ApiModelProperty(value = "退款原因", required = false, example = "测试")
    private String reason;
}
