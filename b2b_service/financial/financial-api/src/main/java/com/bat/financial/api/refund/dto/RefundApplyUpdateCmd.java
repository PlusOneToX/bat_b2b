package com.bat.financial.api.refund.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 16:07
 */
@Data
@ApiModel(value = "RefundApplyUpdateCmd", description = "退款申请单更新")
public class RefundApplyUpdateCmd {

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;

    @NotBlank(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "众芯")
    private String distributorName;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "业务类型 1订单取消 2订单变更", required = true, example = "1")
    private Short businessTypes;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "业务单号", required = true, example = "10000")
    private Integer businessId;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "退款金额", required = true, example = "1")
    private BigDecimal amount;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式", required = true, example = "2")
    private Short refundType;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "处理状态 0 未处理 1 已处理 2 已取消", required = true, example = "0")
    private Short applyStatus;

    @ApiModelProperty(value = "备注", example = "1")
    private String remark;

    @ApiModelProperty(value = "操作人id")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

}
