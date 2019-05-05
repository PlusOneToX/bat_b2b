package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.util.List;

public class GoodsItemPriceRpcQry implements Serializable {

    private Integer distributorId;
    /**
     * 是否获取建议零售价：1是 0否
     */
    private Short retailPriceFlag;
    private List<GoodsItemRpc> goodsItems;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public List<GoodsItemRpc> getGoodsItems() {
        return goodsItems;
    }

    public void setGoodsItems(List<GoodsItemRpc> goodsItems) {
        this.goodsItems = goodsItems;
    }

    public Short getRetailPriceFlag() {
        return retailPriceFlag;
    }

    public void setRetailPriceFlag(Short retailPriceFlag) {
        this.retailPriceFlag = retailPriceFlag;
    }
}
