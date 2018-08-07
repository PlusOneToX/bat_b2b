package com.bat.financial.api.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/21 9:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "CancelTradeCmd", description = "取消交易")
public class CloseTradeCmd extends BaseTrade {
    @ApiModelProperty(value = "商户订单号", example = "bat20187")
    private String outTradeNo;
}
