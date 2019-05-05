package com.bat.dubboapi.thirdparty.erp.dto.goods.data;


import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsItemBoxRpcDTO implements Serializable {
    /**
     * 货品erp编码
     */
    private String itemCode;
    /**
     * 箱子名称
     */
    private String boxName;
    /**
     * 箱子类型
     */
    private String boxType;
    /**
     * 箱子长度
     */
    private BigDecimal boxLength;
    /**
     * 箱子高度
     */
    private BigDecimal boxHeight;
    /**
     * 箱子宽度
     */
    private BigDecimal boxWidth;
    /**
     * 箱子重量
     */
    private BigDecimal boxWeight;
    /**
     * 箱子装箱数量
     */
    private BigDecimal boxNum;
    /**
     * 箱子erpID
     */
    private String boxErpId;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
}
