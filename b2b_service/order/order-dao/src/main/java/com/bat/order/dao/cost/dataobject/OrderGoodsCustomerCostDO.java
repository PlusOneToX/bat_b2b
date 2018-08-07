package com.bat.order.dao.cost.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderGoodsCustomerCostDO {
    private Integer id;

    private Integer orderId;

    private Integer orderGoodsId;

    private Integer customerId;

    private Short itemType;

    private BigDecimal salePrice;

    private BigDecimal actualPrice;

    private Integer goodsPromotionId;

    private Integer orderPromotionId;

    private Integer spellGroupId;

    private Date createTime;

    private Date updateTime;

    private String couponNo;

    private Short couponMethod;

    private Short deliveryFeeFlag;
}