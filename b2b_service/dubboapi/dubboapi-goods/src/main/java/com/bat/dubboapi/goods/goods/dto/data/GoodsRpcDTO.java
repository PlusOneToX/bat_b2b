package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;

public class GoodsRpcDTO implements Serializable {

    private Integer goodsId;
    private String goodsName;
    private String goodsNo;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }
}
