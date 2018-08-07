package com.bat.financial.api.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 9:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "QueryTradeCmd", description = "查询交易")
public class QueryTradeQry extends BaseTrade {
    /**
     * 如果是订单 可以根据订单号 查询 outTradeNo，但是充值的情况 只能直接使用 outTradeNo
     */
    @ApiModelProperty(value = "订单id 一个订单号", example = "100000")
    private String orderId;

    @ApiModelProperty(value = "客户id（可以是C端客户id,也可以是B端客户Id）", required = true, example = "100000")
    private Integer customerId;

    @ApiModelProperty(value = "商户订单号", example = "bat20187")
    private String outTradeNo;
}
