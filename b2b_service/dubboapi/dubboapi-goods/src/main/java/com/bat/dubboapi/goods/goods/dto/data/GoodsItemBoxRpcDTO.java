package com.bat.dubboapi.goods.goods.dto.data;


import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsItemBoxRpcDTO implements Serializable {
    private Integer id;
    private Integer goodsItemId;
    private String boxName;
    private String boxType;
    private BigDecimal boxLength;
    private BigDecimal boxHeight;
    private BigDecimal boxWidth;
    private BigDecimal boxWeight;
    private BigDecimal boxNum;
    private String boxErpId;
    private Integer sort;
    private Short defaultFlag;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(Integer goodsItemId) {
        this.goodsItemId = goodsItemId;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public BigDecimal getBoxLength() {
        return boxLength;
    }

    public void setBoxLength(BigDecimal boxLength) {
        this.boxLength = boxLength;
    }

    public BigDecimal getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(BigDecimal boxHeight) {
        this.boxHeight = boxHeight;
    }

    public BigDecimal getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(BigDecimal boxWidth) {
        this.boxWidth = boxWidth;
    }

    public BigDecimal getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(BigDecimal boxWeight) {
        this.boxWeight = boxWeight;
    }

    public BigDecimal getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(BigDecimal boxNum) {
        this.boxNum = boxNum;
    }

    public String getBoxErpId() {
        return boxErpId;
    }

    public void setBoxErpId(String boxErpId) {
        this.boxErpId = boxErpId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Short defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
