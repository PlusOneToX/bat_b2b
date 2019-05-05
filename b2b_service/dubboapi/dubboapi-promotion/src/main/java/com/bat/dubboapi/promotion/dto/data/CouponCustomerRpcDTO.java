package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CouponCustomerRpcDTO implements Serializable {

    private Integer id;
    private Integer couponId;
    private String couponNo;
    private Integer pieces;
    private String couponName;
    private String couponDesc;
    private String couponExplain;
    private String invalidExplain;
    private Short couponStatus;
    private Date startTime;
    private Date endTime;
    private Short couponMethod;
    private BigDecimal orderMoney;
    private BigDecimal reduction;
    private BigDecimal discount;
    private Short couponType;
    private Short deliveryFeeFlag;
    private BigDecimal deliveryFee;
    private Short modelScope;
    private Short materialScope;
    private Short receivedFlag;
    private Short goodsEnable;
    private Short amountEnable;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public String getCouponExplain() {
        return couponExplain;
    }

    public void setCouponExplain(String couponExplain) {
        this.couponExplain = couponExplain;
    }

    public String getInvalidExplain() {
        return invalidExplain;
    }

    public void setInvalidExplain(String invalidExplain) {
        this.invalidExplain = invalidExplain;
    }

    public Short getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Short couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getCouponMethod() {
        return couponMethod;
    }

    public void setCouponMethod(Short couponMethod) {
        this.couponMethod = couponMethod;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getReduction() {
        return reduction;
    }

    public void setReduction(BigDecimal reduction) {
        this.reduction = reduction;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Short getCouponType() {
        return couponType;
    }

    public void setCouponType(Short couponType) {
        this.couponType = couponType;
    }

    public Short getDeliveryFeeFlag() {
        return deliveryFeeFlag;
    }

    public void setDeliveryFeeFlag(Short deliveryFeeFlag) {
        this.deliveryFeeFlag = deliveryFeeFlag;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Short getModelScope() {
        return modelScope;
    }

    public void setModelScope(Short modelScope) {
        this.modelScope = modelScope;
    }

    public Short getMaterialScope() {
        return materialScope;
    }

    public void setMaterialScope(Short materialScope) {
        this.materialScope = materialScope;
    }

    public Short getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(Short receivedFlag) {
        this.receivedFlag = receivedFlag;
    }

    public Short getGoodsEnable() {
        return goodsEnable;
    }

    public void setGoodsEnable(Short goodsEnable) {
        this.goodsEnable = goodsEnable;
    }

    public Short getAmountEnable() {
        return amountEnable;
    }

    public void setAmountEnable(Short amountEnable) {
        this.amountEnable = amountEnable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
