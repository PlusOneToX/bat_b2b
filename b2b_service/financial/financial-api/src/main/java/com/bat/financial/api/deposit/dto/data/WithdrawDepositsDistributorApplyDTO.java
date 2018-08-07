package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 17:42
 */
@Data
public class WithdrawDepositsDistributorApplyDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
    @ApiModelProperty(value = "分销商名称")
    private String distributorName;
    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawAmount;
    @ApiModelProperty(value = "确认状态 1,申请中,待确认 2,已确认 3,已拒绝")
    private Short applyStatus;
    @ApiModelProperty(value = "提现账户类型： 1.支付宝,2.微信,3.银行")
    private Short withdrawAccountType;
    @ApiModelProperty(value = " 支付宝或微信账户")
    private String wxAlipayAccount;
    @ApiModelProperty(value = "银行卡号")
    private String cardNo;
    @ApiModelProperty(value = "银行卡号")
    private String payee;
    @ApiModelProperty(value = "收款银行")
    private String bankName;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}
