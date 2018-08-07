package com.bat.order.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderGoodsExchangeCodeListDTO {


    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "B2B订单id")
    private Integer orderId;

    @ApiModelProperty(value = "B2B订单号")
    private String orderNo;

    @ApiModelProperty(value = "收货人")
    private String userName;


    @ApiModelProperty(value = "收货人手机号码")
    private String mobile;

    @ApiModelProperty(value = "活动名称")
    private String codeName;

    @ApiModelProperty(value = "明码")
    private String plainCode;

    @ApiModelProperty(value = "批次Id")
    private Integer exchangeFactoryId;

    @ApiModelProperty(value = "盒码")
    private String boxCode;

    @ApiModelProperty(value = "暗码")
    private String secretCode;

    @ApiModelProperty(value = "兑换卡归属订单号")
    private Integer exchangeOrderId;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成")
    private Short orderStatus;

    @ApiModelProperty(value = "付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    private Short payWay;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;
}
