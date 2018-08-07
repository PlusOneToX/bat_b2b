package com.bat.financial.api.refund.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:52
 */
@Data
public class RefundCustomerApplyDTO {
    private Integer id;

    @ApiModelProperty(value = "退款接收方C端客户id")
    protected Integer customerId;

    @ApiModelProperty(value = "C端客户用户名")
    protected String customerName;

    @ApiModelProperty(value = "业务类型 1订单取消 2订单变更")
    private Short businessTypes;

    @ApiModelProperty(value = "业务单号")
    private Integer businessId;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式")
    private Short refundType;

    @ApiModelProperty(value = "处理状态 0 未处理 1 已处理 2 已取消")
    private Short applyStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作人id")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
