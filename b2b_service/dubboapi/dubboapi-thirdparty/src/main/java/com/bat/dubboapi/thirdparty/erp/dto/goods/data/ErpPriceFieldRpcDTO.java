package com.bat.dubboapi.thirdparty.erp.dto.goods.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * erp接口请求参数
 */
public class ErpPriceFieldRpcDTO implements Serializable {

    private Integer goodsItemGradeId;
    private BigDecimal price;

    public Integer getGoodsItemGradeId() {
        return goodsItemGradeId;
    }

    public void setGoodsItemGradeId(Integer goodsItemGradeId) {
        this.goodsItemGradeId = goodsItemGradeId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
