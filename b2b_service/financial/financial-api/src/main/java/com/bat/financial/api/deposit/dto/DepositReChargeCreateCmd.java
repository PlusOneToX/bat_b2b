package com.bat.financial.api.deposit.dto;

import java.math.BigDecimal;

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
@ApiModel(value = "DepositReChargeCreateCmd", description = "充值新增")
public class DepositReChargeCreateCmd {
    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称", required = true, example = "1")
    private String distributorName;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "充值金额", required = true, example = "0.01")
    private BigDecimal amount;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付", required = true, example = "2")
    private Short payWay;

    @ApiModelProperty(value = "备注", example = "测试")
    private String remark;
}
