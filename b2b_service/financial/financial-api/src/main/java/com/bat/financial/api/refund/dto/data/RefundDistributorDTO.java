package com.bat.financial.api.refund.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:54
 */
@Data
public class RefundDistributorDTO {
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商用户名")
    private String distributorName;

    @ApiModelProperty(value = "分销商公司名")
    private String companyName;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "退款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付")
    private Short refundWay;

    @ApiModelProperty(value = "分销商退款凭证id( 线上退款情况才有值)")
    private Integer refundBillsId;

    @ApiModelProperty(value = "C端客户标志 0 不是C端客户(默认B2B) 1是C端客户")
    private Short customerFlag;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "业务类型 1订单退款2充值退款")
    private Short businessType;

    @ApiModelProperty(value = "业务单号(业务类型为1时为订单id)")
    private String businessId;

    @ApiModelProperty(value = "退款单状态,1待确认,2已确认,3已取消")
    private Short refundStatus;

    @ApiModelProperty(value = "")
    private String remark;

    @ApiModelProperty(value = "erp退款单编号")
    private String refundErpNo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
