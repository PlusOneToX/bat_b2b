package com.bat.financial.api.deposit.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/31 16:59
 */
@Data
@ApiModel(value = "DepositReChargeQry", description = "充值查询")
public class DepositReChargeQry {
    @ApiModelProperty(value = "分销商id", example = "1")
    private Integer distributorId;

    @NotBlank(message = "B_RECHARGE_OUT_TRADE_NO_NULL")
    @ApiModelProperty(value = "充值凭证id", required = true, example = "wxce1397364866422439936")
    private String outTradeNo;

    @ApiModelProperty(value = "C端客户标志 0 不是C端客户(默认B2B) 1是C端客户", example = "0")
    private Short customerFlag = 0;

    @ApiModelProperty(value = "充值金额", example = "0.01")
    private BigDecimal amount;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付", required = true, example = "2")
    private Short payWay;

}
