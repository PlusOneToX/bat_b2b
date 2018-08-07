package com.bat.financial.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class OrderSubAccountPageDTO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "分销商ID")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "B2B订单id")
    private Integer orderId;

    @ApiModelProperty(value = "B2B订单号")
    private String orderNo;

    @ApiModelProperty(value = "微信流水号、支付凭证")
    private String transactionId;

    @ApiModelProperty(value = "门店ID")
    private Integer shopId;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "最大分账金额")
    private BigDecimal maxSubAccountAmount;

    @ApiModelProperty(value = "利润金额、按照利润分账有值")
    private BigDecimal profitAccount;

    @ApiModelProperty(value = "订单实际分账金额")
    private BigDecimal actualSubAccountAmount;

    @ApiModelProperty(value = "分账状态（前台展示的、与后台不一致、需要加入是否分账失败）0、不分账 1、待分账 2、部分分账 3、全部分账 4、分账失败")
    private Short status;

    @ApiModelProperty(value = "是否存在分账失败 1、是 0、否")
    private Short subAccountFailFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}