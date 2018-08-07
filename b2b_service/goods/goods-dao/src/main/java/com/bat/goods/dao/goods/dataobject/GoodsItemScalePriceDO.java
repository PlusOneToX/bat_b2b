package com.bat.goods.dao.goods.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsItemScalePriceDO {

    private Integer id;
    private Integer goodsId;
    private Integer goodsItemId;
    private Integer goodsItemGradeId;
    private BigDecimal price;

}
