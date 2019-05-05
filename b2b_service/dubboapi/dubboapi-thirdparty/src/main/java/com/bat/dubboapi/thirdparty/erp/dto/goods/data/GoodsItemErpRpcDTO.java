package com.bat.dubboapi.thirdparty.erp.dto.goods.data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GoodsItemErpRpcDTO implements Serializable {
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 重量（g）
     */
    private BigDecimal weight;
    /**
     * 货品名称
     */
    private String itemName;
    /**
     * 货品名称（英文）
     */
//    private String itemNameEn;
    /**
     * 货品编码
     */
    private String itemCode;
    /**
     * 货品erp内码
     */
    private Integer itemErpId;
    /**
     * 货品品类erp编码
     */
    private String categoryErpNo;
    /**
     * 长
     */
    private BigDecimal length;
    /**
     * 宽
     */
    private BigDecimal width;
    /**
     * 高
     */
    private BigDecimal height;
    /**
     * 单位
     */
    private String unit;
    /**
     * 预售最少购买数量
     */
    private String moq;
    /**
     * 默认销售价
     */
    private BigDecimal salePrice = new BigDecimal(0);
    /**
     * 成本价
     */
    private BigDecimal costPrice = new BigDecimal(0);
    /**
     * 商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止
     */
    private Short lifeCycle = 1;
    /**
     * ERP促销状态 清仓 5caebbb86c7863 ; BCD  5e9fec57c26d75
     */
    private String promotionStatus;

    /**
     * 货品等级价格列表（按顺序）
     */
    private List<ErpPriceFieldRpcDTO> priceList;
    /**
     * 货品装箱规格
     */
    private List<GoodsItemBoxRpcDTO> boxInfoList;


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public Integer getItemErpId() {
        return itemErpId;
    }

    public void setItemErpId(Integer itemErpId) {
        this.itemErpId = itemErpId;
    }

    public String getCategoryErpNo() {
        return categoryErpNo;
    }

    public void setCategoryErpNo(String categoryErpNo) {
        this.categoryErpNo = categoryErpNo;
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

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Short getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(Short lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(String promotionStatus) {
        this.promotionStatus = promotionStatus;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ErpPriceFieldRpcDTO> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<ErpPriceFieldRpcDTO> priceList) {
        this.priceList = priceList;
    }

    public List<GoodsItemBoxRpcDTO> getBoxInfoList() {
        return boxInfoList;
    }

    public void setBoxInfoList(List<GoodsItemBoxRpcDTO> boxInfoList) {
        this.boxInfoList = boxInfoList;
    }
}
