package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsConditionRpcCmd implements Serializable {
    private Short reduceOrPresent;
    private Short specialFlag;
    private Integer oneBuyCount;
    private BigDecimal oneBuyMoney;
    private Short reduceType;
    private BigDecimal discount;
    private BigDecimal reduction;

    List<GoodsConditionSpecialRpcCmd> specials;

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

    public Short getReduceType() {
        return reduceType;
    }

    public void setReduceType(Short reduceType) {
        this.reduceType = reduceType;
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

    public List<GoodsConditionSpecialRpcCmd> getSpecials() {
        return specials;
    }

    public void setSpecials(List<GoodsConditionSpecialRpcCmd> specials) {
        this.specials = specials;
    }

    public Short getReduceOrPresent() {
        return reduceOrPresent;
    }

    public void setReduceOrPresent(Short reduceOrPresent) {
        this.reduceOrPresent = reduceOrPresent;
    }
}
