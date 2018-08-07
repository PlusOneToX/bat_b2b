package com.bat.financial.api.refund.dto;

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
public class RefundApplyManualConfirmCmd {

    @NotNull(message = "P_REFUND_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_REFUND_RECEIVER_TYPE_ID_NULL")
    @ApiModelProperty(value = "接收方类型 1分销商 2 C端客户", required = true, example = "1")
    private Short receiverType;

    @ApiModelProperty(value = "退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式", required = false, example = "2")
    private Short refundType;

    @ApiModelProperty(value = "处理状态 0 未处理 1 已处理 2 已取消", required = false, example = "0")
    private Short applyStatus;

    @ApiModelProperty(value = "备注", example = "1")
    private String remark;

    @ApiModelProperty(value = "操作人id")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

}
