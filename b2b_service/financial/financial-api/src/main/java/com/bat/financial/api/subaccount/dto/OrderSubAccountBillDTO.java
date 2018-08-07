package com.bat.financial.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderSubAccountBillDTO {

    @ApiModelProperty(value = "分账流水id")
    private Integer id;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "最大分账金额、默认")
    private BigDecimal maxSubAccountAmount;

    @ApiModelProperty(value = "实际分账金额")
    private BigDecimal actualSubAccountAmount;


    @ApiModelProperty(value = "分账状态 0、不分账 1、待分账 2、部分分账 3、全部分账")
    private Short status;

    @ApiModelProperty(value = "业务员openid")
    private String openId;

    @ApiModelProperty(value = "商户号")
    private String merchantNumber;

    @ApiModelProperty(value = "单号")
    private String outTradeNo;


    @ApiModelProperty(value = "业务员姓名")
    private String salemanName;

    @ApiModelProperty(value = "是否分账成功 1、是 0、否")
    private Short successFlag;

    @ApiModelProperty(value = "失败原因")
    private String failReason;

    @ApiModelProperty(value = "最后处理时间")
    private Date updateTime;

    @ApiModelProperty(value = "成功分账时间")
    private Date successTime;


}