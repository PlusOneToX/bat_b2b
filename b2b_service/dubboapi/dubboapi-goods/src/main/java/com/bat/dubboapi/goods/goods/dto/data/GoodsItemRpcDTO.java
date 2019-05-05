package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsItemRpcDTO implements Serializable {
    private Integer id;
    private Integer goodsId;
    private String itemName;
    private String itemNameEn;
    private String itemCode;
    private String barCode;
    private String goodsName;
    private String goodsNameEn;
    private String goodsNo;
    private Short goodsType;
    private Short diyType;
    private Integer specsId;
    private String specsName;
    private String specsNameEn;
    private Integer colorId;
    private String colorName;
    private String colorNameEn;
    private Integer brandId;
    private String brandName;
    private Integer categoryId;
    private String categoryName;
    private String brandNameEn;
    private String categoryNameEn;
    private String imageUrl1;
    private String imageUrl1en;
    private String itemImg;
    private Short advanceSaleFlag;
    private Short saleStatus;
    /**
     * 直发客户是否支持在途：0-否 1-是
     */
    private Short onwaySaleFlag;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    private String moq;

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Short getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Short saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Short getAdvanceSaleFlag() {
        return advanceSaleFlag;
    }

    public void setAdvanceSaleFlag(Short advanceSaleFlag) {
        this.advanceSaleFlag = advanceSaleFlag;
    }

    public Short getOnwaySaleFlag() {
        return onwaySaleFlag;
    }

    public void setOnwaySaleFlag(Short onwaySaleFlag) {
        this.onwaySaleFlag = onwaySaleFlag;
    }

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

    public String getImageUrl1en() {
        return imageUrl1en;
    }

    public void setImageUrl1en(String imageUrl1en) {
        this.imageUrl1en = imageUrl1en;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getBrandNameEn() {
        return brandNameEn;
    }

    public void setBrandNameEn(String brandNameEn) {
        this.brandNameEn = brandNameEn;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public void setCategoryNameEn(String categoryNameEn) {
        this.categoryNameEn = categoryNameEn;
    }

    public Integer getSpecsId() {
        return specsId;
    }

    public void setSpecsId(Integer specsId) {
        this.specsId = specsId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemNameEn() {
        return itemNameEn;
    }

    public void setItemNameEn(String itemNameEn) {
        this.itemNameEn = itemNameEn;
    }

    public String getGoodsNameEn() {
        return goodsNameEn;
    }

    public void setGoodsNameEn(String goodsNameEn) {
        this.goodsNameEn = goodsNameEn;
    }

    public String getSpecsNameEn() {
        return specsNameEn;
    }

    public void setSpecsNameEn(String specsNameEn) {
        this.specsNameEn = specsNameEn;
    }

    public String getColorNameEn() {
        return colorNameEn;
    }

    public void setColorNameEn(String colorNameEn) {
        this.colorNameEn = colorNameEn;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSpecsName() {
        return specsName;
    }

    public void setSpecsName(String specsName) {
        this.specsName = specsName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
