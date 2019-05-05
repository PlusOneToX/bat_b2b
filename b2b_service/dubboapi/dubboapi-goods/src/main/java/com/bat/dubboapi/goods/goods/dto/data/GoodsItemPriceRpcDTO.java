package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsItemPriceRpcDTO implements Serializable {

    private Integer goodsId;
    private Integer itemId;
    private BigDecimal retailPrice;
    private BigDecimal salePrice;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
