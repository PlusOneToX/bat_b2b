package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsPromotionPriceDO {
    private Integer id;

    private Integer goodsId;

    private Short specialFlag;

    private Short reduceOrPresent;

    private Short groupSeckillType;

    private Integer scalePriceId;

    private Integer promotionId;

    private Integer groupSeckillId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal promotionMinPrice;

    private BigDecimal promotionMaxPrice;
}