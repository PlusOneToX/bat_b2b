package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsConditionSpecialRpcCmd implements Serializable {
    private Integer itemId;
    private BigDecimal specialPrice;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }
}
