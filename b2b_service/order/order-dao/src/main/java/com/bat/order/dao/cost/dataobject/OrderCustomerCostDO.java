package com.bat.order.dao.cost.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderCustomerCostDO {
    private Integer id;

    private Integer orderId;

    private Integer customerId;

    private BigDecimal goodsAmount;

    private BigDecimal distributionAmount;

    private BigDecimal goodsPromotionAmount;

    private BigDecimal orderPromotionAmount;

    private BigDecimal orderAmount;

    private BigDecimal payAmount;

    private BigDecimal paidAmount;

    private BigDecimal refundedAmount;

    private String outTradeNo;

    private Date createTime;

    private Date updateTime;

    private BigDecimal orderCouponAmount;

    /**
     * 扩展属性
     */
    private BigDecimal alipayVoucherAmount;

}