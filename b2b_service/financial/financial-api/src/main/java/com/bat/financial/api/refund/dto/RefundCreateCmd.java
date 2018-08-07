package com.bat.financial.api.refund.dto;

import java.math.BigDecimal;

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
@ApiModel(value = "RefundCreateCmd", description = "退款单新增")
public class RefundCreateCmd {

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "1")
    private Integer distributorId;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "众芯")
    private String distributorName;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商公司名称", required = true, example = "深圳市众芯能科技有限公司")
    private String companyName;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "退款金额", required = true, example = "1")
    private BigDecimal amount;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "退款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付", required = true, example = "1")
    private Short refundWay;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商退款凭证id( 线上退款情况才有值)", required = true, example = "1")
    private Integer refundBillsId;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "C端客户标志 0 不是C端客户(默认B2B) 1是C端客户", required = true, example = "1")
    private Short customerFlag;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "币种", required = true, example = "CNY")
    private String currencyType;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "业务类型 1订单退款2充值退款", required = true, example = "1")
    private Short businessType;

    @ApiModelProperty(value = "业务单号(业务类型为1时为订单id)", example = "1")
    private Integer businessId;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "退款单状态,1待确认,2已确认,3已取消", required = true, example = "1")
    private Short refundStatus;

    @ApiModelProperty(value = "备注", example = "1")
    private String remark;

    @NotNull(message = "P_RECHARGE_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "erp退款单编号", example = "1")
    private String refundErpNo;
}
