package com.bat.dubboapi.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class GoodsItemPriceRpcQry implements Serializable {
    private Integer serialNumber;
    private Integer goodsId;
    private Integer itemId;
    private Integer materialId;
    private Integer modelId;
    /**
     * 会员价
     */
    private BigDecimal salePrice;
    /**
     * 货品总数量
     */
    private Integer itemCount;
    /**
     * 在库货品数量
     */
    private Integer itemInCount;
    /**
     * vmi货品数量
     */
    private Integer itemVmiCount;
    private Integer itemMtoCount;
    /**
     * 在途货品数量
     */
    private Integer itemOnWayCount;
    private Integer goodsPromotionId;
    private Integer orderPromotionId;
    private Integer spellGroupId;
    private String couponNo;
    /**
     * 是否赠品 1 非赠品 2 赠品
     */
    private Short itemType;
    /**
     * 是否预售 1 是 0 否
     */
    private Short mtoType;

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getItemMtoCount() {
        return itemMtoCount;
    }

    public void setItemMtoCount(Integer itemMtoCount) {
        this.itemMtoCount = itemMtoCount;
    }

    public Short getMtoType() {
        return mtoType;
    }

    public void setMtoType(Short mtoType) {
        this.mtoType = mtoType;
    }

    public Integer getGoodsPromotionId() {
        return goodsPromotionId;
    }

    public void setGoodsPromotionId(Integer goodsPromotionId) {
        this.goodsPromotionId = goodsPromotionId;
    }

    public Integer getOrderPromotionId() {
        return orderPromotionId;
    }

    public void setOrderPromotionId(Integer orderPromotionId) {
        this.orderPromotionId = orderPromotionId;
    }

    public Integer getSpellGroupId() {
        return spellGroupId;
    }

    public void setSpellGroupId(Integer spellGroupId) {
        this.spellGroupId = spellGroupId;
    }

    public Short getItemType() {
        return itemType;
    }

    public void setItemType(Short itemType) {
        this.itemType = itemType;
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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getItemInCount() {
        return itemInCount;
    }

    public void setItemInCount(Integer itemInCount) {
        this.itemInCount = itemInCount;
    }

    public Integer getItemVmiCount() {
        return itemVmiCount;
    }

    public void setItemVmiCount(Integer itemVmiCount) {
        this.itemVmiCount = itemVmiCount;
    }

    public Integer getItemOnWayCount() {
        return itemOnWayCount;
    }

    public void setItemOnWayCount(Integer itemOnWayCount) {
        this.itemOnWayCount = itemOnWayCount;
    }
}
