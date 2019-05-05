package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class PromotionRuleConditionRpcDTO implements Serializable {
    private Integer id;
    private Integer promotionId;
    private Integer promotionRuleId;
    private String conditionName;
    private String conditionNameEn;
    private Short specialFlag;
    private Integer oneBuyCount;
    private BigDecimal oneBuyMoney;
    private Short reduceOrPresent;
    private Short reduceType;
    private Short reductionPresentAddFlag;
    private BigDecimal discount;
    private BigDecimal reduction;
    private Integer presentCount;
    private List<PromotionRuleConditionPresentRpcDTO> presents;
    private BigDecimal specialPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getPromotionRuleId() {
        return promotionRuleId;
    }

    public void setPromotionRuleId(Integer promotionRuleId) {
        this.promotionRuleId = promotionRuleId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getConditionNameEn() {
        return conditionNameEn;
    }

    public void setConditionNameEn(String conditionNameEn) {
        this.conditionNameEn = conditionNameEn;
    }

    public Short getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(Short specialFlag) {
        this.specialFlag = specialFlag;
    }

    public Integer getOneBuyCount() {
        return oneBuyCount;
    }

    public void setOneBuyCount(Integer oneBuyCount) {
        this.oneBuyCount = oneBuyCount;
    }

    public BigDecimal getOneBuyMoney() {
        return oneBuyMoney;
    }

    public void setOneBuyMoney(BigDecimal oneBuyMoney) {
        this.oneBuyMoney = oneBuyMoney;
    }

    public Short getReduceOrPresent() {
        return reduceOrPresent;
    }

    public void setReduceOrPresent(Short reduceOrPresent) {
        this.reduceOrPresent = reduceOrPresent;
    }

    public Short getReduceType() {
        return reduceType;
    }

    public void setReduceType(Short reduceType) {
        this.reduceType = reduceType;
    }

    public Short getReductionPresentAddFlag() {
        return reductionPresentAddFlag;
    }

    public void setReductionPresentAddFlag(Short reductionPresentAddFlag) {
        this.reductionPresentAddFlag = reductionPresentAddFlag;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getReduction() {
        return reduction;
    }

    public void setReduction(BigDecimal reduction) {
        this.reduction = reduction;
    }

    public Integer getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Integer presentCount) {
        this.presentCount = presentCount;
    }

    public List<PromotionRuleConditionPresentRpcDTO> getPresents() {
        return presents;
    }

    public void setPresents(List<PromotionRuleConditionPresentRpcDTO> presents) {
        this.presents = presents;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }
}
