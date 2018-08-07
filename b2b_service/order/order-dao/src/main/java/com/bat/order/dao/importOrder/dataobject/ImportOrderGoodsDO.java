package com.bat.order.dao.importOrder.dataobject;

import java.math.BigDecimal;

public class ImportOrderGoodsDO {
    private Integer id;

    private Integer importOrderId;

    private Integer itemId;

    private String itemCode;

    private String itemName;

    private Integer goodsId;

    private String goodsName;

    private String goodsNo;

    private BigDecimal distributorPrice;

    private BigDecimal promotionAmount;

    private BigDecimal actualPrice;

    private Integer itemCount;

    private Integer actualOrderCount;

    private Integer itemLoseCount;

    private Integer inWarehouseCount;

    private Integer onWayCount;

    private Short goodsType;

    private Integer specificationValueId;

    private Integer colorValueId;

    private Integer itemType;

    private Integer gradeDiscountId;

    private Integer ruleId;

    private Integer orderRuleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportOrderId() {
        return importOrderId;
    }

    public void setImportOrderId(Integer importOrderId) {
        this.importOrderId = importOrderId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

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
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo == null ? null : goodsNo.trim();
    }

    public BigDecimal getDistributorPrice() {
        return distributorPrice;
    }

    public void setDistributorPrice(BigDecimal distributorPrice) {
        this.distributorPrice = distributorPrice;
    }

    public BigDecimal getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(BigDecimal promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getActualOrderCount() {
        return actualOrderCount;
    }

    public void setActualOrderCount(Integer actualOrderCount) {
        this.actualOrderCount = actualOrderCount;
    }

    public Integer getItemLoseCount() {
        return itemLoseCount;
    }

    public void setItemLoseCount(Integer itemLoseCount) {
        this.itemLoseCount = itemLoseCount;
    }

    public Integer getInWarehouseCount() {
        return inWarehouseCount;
    }

    public void setInWarehouseCount(Integer inWarehouseCount) {
        this.inWarehouseCount = inWarehouseCount;
    }

    public Integer getOnWayCount() {
        return onWayCount;
    }

    public void setOnWayCount(Integer onWayCount) {
        this.onWayCount = onWayCount;
    }

    public Short getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getSpecificationValueId() {
        return specificationValueId;
    }

    public void setSpecificationValueId(Integer specificationValueId) {
        this.specificationValueId = specificationValueId;
    }

    public Integer getColorValueId() {
        return colorValueId;
    }

    public void setColorValueId(Integer colorValueId) {
        this.colorValueId = colorValueId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getGradeDiscountId() {
        return gradeDiscountId;
    }

    public void setGradeDiscountId(Integer gradeDiscountId) {
        this.gradeDiscountId = gradeDiscountId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getOrderRuleId() {
        return orderRuleId;
    }

    public void setOrderRuleId(Integer orderRuleId) {
        this.orderRuleId = orderRuleId;
    }
}