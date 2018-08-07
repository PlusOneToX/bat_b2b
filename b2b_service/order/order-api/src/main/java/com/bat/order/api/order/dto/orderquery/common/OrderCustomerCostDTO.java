package com.bat.order.api.order.dto.orderquery.common;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/7 10:28
 */
@Data
public class OrderCustomerCostDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("C端客户id")
    private Integer customerId;
    @ApiModelProperty("商品总价")
    private BigDecimal goodsAmount;
    @ApiModelProperty("物流费用")
    private BigDecimal distributionAmount;
    @ApiModelProperty("商品促销金额")
    private BigDecimal goodsPromotionAmount;
    @ApiModelProperty("订单促销金额")
    private BigDecimal orderPromotionAmount;
    @ApiModelProperty("订单总额")
    private BigDecimal orderAmount;
    @ApiModelProperty("应付款总额")
    private BigDecimal payAmount;
    @ApiModelProperty("已付款金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("支付凭证号(注意：全平台唯一)")
    private String outTradeNo;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("订单优惠券优惠金额")
    private BigDecimal orderCouponAmount;

    @Override
    public OrderCustomerCostDTO clone() throws CloneNotSupportedException {
        return (OrderCustomerCostDTO)super.clone();
    }

}
