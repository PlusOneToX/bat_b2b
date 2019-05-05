package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;

public class UserGoodsRpcDTO implements Serializable {

    private Integer id;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private String imageUrl1;
    private String imageUrl1en;
    private Short goodsType;
    private Short diyType;

    public Short getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    public Short getDiyType() {
        return diyType;
    }

    public void setDiyType(Short diyType) {
        this.diyType = diyType;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl1en() {
        return imageUrl1en;
    }

    public void setImageUrl1en(String imageUrl1en) {
        this.imageUrl1en = imageUrl1en;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNameEn() {
        return goodsNameEn;
    }

    public void setGoodsNameEn(String goodsNameEn) {
        this.goodsNameEn = goodsNameEn;
    }
}
