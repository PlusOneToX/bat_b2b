package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;

public class GoodsItemRpc implements Serializable {

    private Integer goodsId;
    private Integer itemId;
    private Integer ItemErpId;
    private String goodsNo;
    private String itemCode;
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemErpId() {
        return ItemErpId;
    }

    public void setItemErpId(Integer itemErpId) {
        ItemErpId = itemErpId;
    }

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

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
