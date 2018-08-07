package com.bat.financial.api.voucher.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/5 19:39
 */
@Data
@ApiModel(value = "VoucherDistributorDTO", description = "收款单")
public class VoucherDistributorDTO {
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商用户名")
    private String distributorName;

    @ApiModelProperty(value = "分销商公司名")
    private String companyName;

    @ApiModelProperty(value = "收款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付")
    private Short payWay;

    @ApiModelProperty(value = "分销客户支付凭证id(现款线上支付情况才有值)")
    private Integer payBillsId;

    @ApiModelProperty(value = "C端客户标志 0 不是C端客户(默认B2B) 1是C端客户")
    private Short customerFlag;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "业务类型 1订单收款2在线充值收款")
    private Short businessType;

    @ApiModelProperty(value = "业务单号(业务类型为1时为订单id)")
    private String businessId;

    @ApiModelProperty(value = "收款单状态,1待确认,2已确认,3已取消")
    private Short voucherStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "erp收款单编号")
    private String voucherErpNo;

    @ApiModelProperty(value = "支付凭证号")
    private String outTradeNo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
