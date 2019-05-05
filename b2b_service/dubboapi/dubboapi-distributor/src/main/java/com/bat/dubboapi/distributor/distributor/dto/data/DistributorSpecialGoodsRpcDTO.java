package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class DistributorSpecialGoodsRpcDTO implements Serializable {

    private Integer goodsItemId;
    private Integer goodsId;
    private BigDecimal distributorPrice;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(Integer goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public BigDecimal getDistributorPrice() {
        return distributorPrice;
    }

    public void setDistributorPrice(BigDecimal distributorPrice) {
        this.distributorPrice = distributorPrice;
    }
}
