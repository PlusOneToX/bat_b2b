package com.bat.financial.dao.subaccount.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSubAccountBillDO {
    private Integer id;

    private Integer orderSubAccountId;

    private Integer levelId;

    private String levelName;

    private BigDecimal maxSubAccountAmount;

    private BigDecimal actualSubAccountAmount;

    private BigDecimal ratio;

    private Short status;

    private String openId;

    private String merchantNumber;

    private String outTradeNo;

    private Integer salemanId;

    private String salemanName;

    private Short successFlag;

    private String failReason;

    private Date createTime;

    private Date updateTime;

    private Date successTime;

    private String subTransactionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderSubAccountId() {
        return orderSubAccountId;
    }

    public void setOrderSubAccountId(Integer orderSubAccountId) {
        this.orderSubAccountId = orderSubAccountId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public BigDecimal getMaxSubAccountAmount() {
        return maxSubAccountAmount;
    }

    public void setMaxSubAccountAmount(BigDecimal maxSubAccountAmount) {
        this.maxSubAccountAmount = maxSubAccountAmount;
    }

    public BigDecimal getActualSubAccountAmount() {
        return actualSubAccountAmount;
    }

    public void setActualSubAccountAmount(BigDecimal actualSubAccountAmount) {
        this.actualSubAccountAmount = actualSubAccountAmount;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber == null ? null : merchantNumber.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Integer getSalemanId() {
        return salemanId;
    }

    public void setSalemanId(Integer salemanId) {
        this.salemanId = salemanId;
    }

    public String getSalemanName() {
        return salemanName;
    }

    public void setSalemanName(String salemanName) {
        this.salemanName = salemanName == null ? null : salemanName.trim();
    }

    public Short getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Short successFlag) {
        this.successFlag = successFlag;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getSubTransactionId() {
        return subTransactionId;
    }

    public void setSubTransactionId(String subTransactionId) {
        this.subTransactionId = subTransactionId;
    }
}