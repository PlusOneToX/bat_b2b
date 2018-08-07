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
 * @date: 2018/5/29 11:57
 */
@Data
@ApiModel(value = "WithdrawalCreateCmd", description = "添加提现")
public class WithdrawalCreateCmd {

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "3758")
    private Integer distributorId;

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "薛国国")
    private String distributorName;

    @NotNull(message = "P_WITHDRAW_AMOUNT_NULL")
    @ApiModelProperty(value = "提现金额", required = true, example = "0.01")
    private BigDecimal withdrawAmount;

    @NotNull(message = "P_WITHDRAW_AMOUNT_TYPE_NULL")
    @ApiModelProperty(value = "提现账户类型 提现账户类型： 1.支付宝,2.微信,3.银行", required = true, example = "1")
    private Short withdrawAccountType;

    @ApiModelProperty(value = "支付宝或微信账户", example = "18995912083")
    private String wxAlipayAccount;

    @NotBlank(message = "P_WITHDRAW_CARD_NO_NULL")
    @ApiModelProperty(value = "银行卡号", required = true, example = "18995912083")
    private String cardNo;

    @NotNull(message = "P_WITHDRAW_PAYEE_NULL")
    @ApiModelProperty(value = "收款人", required = true, example = "薛国国")
    private String payee;

    @ApiModelProperty(value = "收款银行", example = "薛国国")
    private String bankName;

    @ApiModelProperty(value = "备注", example = "薛国国")
    private String remark;

}
