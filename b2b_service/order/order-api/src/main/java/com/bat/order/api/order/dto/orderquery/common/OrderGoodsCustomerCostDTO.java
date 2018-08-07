package com.bat.order.api.order.dto.orderquery.common;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/6 14:47
 */
@Data
public class OrderGoodsCustomerCostDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("订单明细id")
    private Integer orderGoodsId;
    @ApiModelProperty("C端客户id")
    private Integer customerId;
    @ApiModelProperty("是否赠品 1 非赠品 2 赠品")
    private Short itemType;
    @ApiModelProperty("单价")
    private BigDecimal salePrice;
    @ApiModelProperty("实际单价")
    private BigDecimal actualPrice;
    @ApiModelProperty("商品促销活动id(活动条件id)")
    private Integer goodsPromotionId;
    @ApiModelProperty("订单促销活动id(活动条件id)")
    private Integer orderPromotionId;
    @ApiModelProperty("拼团秒杀活动id")
    private Integer spellGroupId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("优惠券码(多个中间用逗号隔开)")
    private String couponNo;
    @ApiModelProperty("优惠形式，1满减  2满折 3兑换")
    private Short couponMethod;
    @ApiModelProperty("是否收取快递费 0否 1是")
    private Short deliveryFeeFlag;
}
