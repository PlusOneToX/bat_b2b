package com.bat.flexible.api.exchange.dto;


import java.io.Serializable;
import java.math.BigDecimal;


public class ExchangeGoodsBean implements Serializable {
    private static final long serialVersionUID = -2125481479517285933L;


    private Integer id;

    private Integer goodsId;

    private Integer itemId;


    //商品名称
    private String goodsName;

    //货品编码
    private String itemCode;

    //条形码
    private String barCode;

    //规格
    private String specificationsName;

    //颜色
    private String colorName;

    //建议零售价
    private BigDecimal retailPrice;

    //建议批发价
    private BigDecimal wholesalePrice;

    private Integer numInWarehouse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSpecificationsName() {
        return specificationsName;
    }

    public void setSpecificationsName(String specificationsName) {
        this.specificationsName = specificationsName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public Integer getNumInWarehouse() {
        return numInWarehouse;
    }

    public void setNumInWarehouse(Integer numInWarehouse) {
        this.numInWarehouse = numInWarehouse;
    }


}
