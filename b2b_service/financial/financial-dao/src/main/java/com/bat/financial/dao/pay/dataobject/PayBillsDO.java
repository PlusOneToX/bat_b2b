package com.bat.financial.dao.pay.dataobject;

import static com.bat.financial.dao.pay.dataobject.ExpireTime.DEFAULT_EXPIRE_TIME;

import java.math.BigDecimal;
import java.util.Date;

public class PayBillsDO {
    protected Integer id;

    protected String outTradeNo;

    protected Short payType;

    protected Short businessType;

    protected String businessId;

    protected Short payStatus;

    protected BigDecimal totalFee;

    protected String orderTitle;

    protected String orderDescribe;

    protected String productId;

    protected String onlineTradeNo;

    protected Date expireTime;

    protected Date payTime;

    protected Date createTime;

    protected Date updateTime;

    protected String payMethod;

    protected Short tradeMode;

    protected Integer payeeId;

    protected Integer organizationId;

    protected String appId;

    protected BigDecimal receiptAmount;

    protected String remark;

    /**
     * 扩展属性 凭证表没有存储
     */
    protected String currencyCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public Short getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Short businessType) {
        this.businessType = businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle == null ? null : orderTitle.trim();
    }

    public String getOrderDescribe() {
        return orderDescribe;
    }

    public void setOrderDescribe(String orderDescribe) {
        this.orderDescribe = orderDescribe == null ? null : orderDescribe.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getOnlineTradeNo() {
        return onlineTradeNo;
    }

    public void setOnlineTradeNo(String onlineTradeNo) {
        this.onlineTradeNo = onlineTradeNo == null ? null : onlineTradeNo.trim();
    }

    public Date getExpireTime() {
        if (expireTime == null) {
            return new Date(getUpdateTime().getTime() + DEFAULT_EXPIRE_TIME * 1000 - 1);
        }
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Short getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(Short tradeMode) {
        this.tradeMode = tradeMode;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PayBillsDO{" +
                "id=" + id +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", payType=" + payType +
                ", businessType=" + businessType +
                ", businessId='" + businessId + '\'' +
                ", payStatus=" + payStatus +
                ", totalFee=" + totalFee +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderDescribe='" + orderDescribe + '\'' +
                ", productId='" + productId + '\'' +
                ", onlineTradeNo='" + onlineTradeNo + '\'' +
                ", expireTime=" + expireTime +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", payMethod='" + payMethod + '\'' +
                ", tradeMode=" + tradeMode +
                ", payeeId=" + payeeId +
                ", organizationId=" + organizationId +
                ", appId='" + appId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", receiptAmount=" + receiptAmount +
                ", remark='" + remark + '\'' +
                '}';
    }
}