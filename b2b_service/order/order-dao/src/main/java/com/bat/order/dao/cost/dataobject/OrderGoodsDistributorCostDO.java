package com.bat.order.dao.cost.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import com.bat.order.dao.order.dataobject.OrderGoodsDO;

import lombok.Data;

@Data
public class OrderGoodsDistributorCostDO {
    private Integer id;
    private Integer orderId;
    private Integer orderGoodsId;

    private Integer distributorId;

    private Short itemType;

    private BigDecimal salePrice;

    private BigDecimal actualPrice;

    private BigDecimal platformPrice;

    private Integer goodsPromotionId;

    private Integer orderPromotionId;

    private Integer spellGroupId;

    private Date createTime;

    private Date updateTime;

    private OrderGoodsDO orderGoodsDO;

    private BigDecimal rebateVoucherAmount;

}