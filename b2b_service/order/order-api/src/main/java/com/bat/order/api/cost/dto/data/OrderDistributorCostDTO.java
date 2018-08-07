package com.bat.order.api.cost.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/22 12:43
 */
@Data
public class OrderDistributorCostDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("归属分销商id")
    private Integer distributorId;
    @ApiModelProperty("商品总价")
    private BigDecimal goodsAmount;
    @ApiModelProperty("物流费用")
    private BigDecimal distributionAmount;
    @ApiModelProperty("商品促销金额")
    private BigDecimal goodsPromotionAmount;
    @ApiModelProperty("订单促销金额")
    private BigDecimal orderPromotionAmount;
    @ApiModelProperty("代金券抵扣金额")
    private BigDecimal rebateVoucherAmount;
    @ApiModelProperty("余额支付金额")
    private BigDecimal depositAmount;
    @ApiModelProperty("应付款总额")
    private BigDecimal payAmount;
    @ApiModelProperty("已付款金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Override
    public OrderDistributorCostDTO clone() throws CloneNotSupportedException {
        return (OrderDistributorCostDTO)super.clone();
    }
}
