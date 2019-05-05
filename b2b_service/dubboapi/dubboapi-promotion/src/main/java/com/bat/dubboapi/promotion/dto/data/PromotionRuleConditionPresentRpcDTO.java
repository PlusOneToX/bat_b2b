package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class PromotionRuleConditionPresentRpcDTO implements Serializable {
    private Integer id;
    private Integer promotionRuleConditionId;
    private Integer goodsId;
    private String goodsNo;
    private String goodsName;
    private Integer itemId;
    private String itemCode;
    private String itemName;
    private Integer count;
    private Integer totalCount;
    private String colorName;
    private String specsName;
    private String colorNameEn;
    private String specsNameEn;
    private String imageUrl1;
    private String imageUrl1en;
    private String itemImg;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPromotionRuleConditionId() {
        return promotionRuleConditionId;
    }

    public void setPromotionRuleConditionId(Integer promotionRuleConditionId) {
        this.promotionRuleConditionId = promotionRuleConditionId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSpecsName() {
        return specsName;
    }

    public void setSpecsName(String specsName) {
        this.specsName = specsName;
    }

    public String getColorNameEn() {
        return colorNameEn;
    }

    public void setColorNameEn(String colorNameEn) {
        this.colorNameEn = colorNameEn;
    }

    public String getSpecsNameEn() {
        return specsNameEn;
    }

    public void setSpecsNameEn(String specsNameEn) {
        this.specsNameEn = specsNameEn;
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

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
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
}
