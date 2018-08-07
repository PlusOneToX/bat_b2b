package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsMinMaxPriceDO {
    private Integer id;
    private Integer goodsId;
    private Integer scalePriceId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
