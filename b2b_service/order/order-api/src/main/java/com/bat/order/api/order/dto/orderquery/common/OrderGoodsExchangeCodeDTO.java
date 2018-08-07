package com.bat.order.api.order.dto.orderquery.common;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/9 17:11
 */
@Data
public class OrderGoodsExchangeCodeDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("订单明细id")
    private Integer orderGoodsId;
    @ApiModelProperty("兑换卡id")
    private Integer exchangeId;
    @ApiModelProperty("暗码")
    private String secretCode;
    @ApiModelProperty("快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）")
    private Short mailSetting;
    @ApiModelProperty("邮费")
    private BigDecimal mailFee;
    @ApiModelProperty("兑换卡归属订单号")
    private Integer exchangeOrderId;
    @ApiModelProperty("兑换卡归属订单明细号")
    private Integer exchangeOrderGoodsId;
    @ApiModelProperty("发卡分销商id")
    private Integer distributorId;
    @ApiModelProperty("发卡分销商公司名")
    private String distributorCompanyName;
    @ApiModelProperty("业务员id")
    private Integer salesId;
    @ApiModelProperty("业务员姓名")
    private String salesName;
}
