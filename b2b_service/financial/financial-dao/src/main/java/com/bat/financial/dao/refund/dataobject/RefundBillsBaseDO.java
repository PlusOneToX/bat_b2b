package com.bat.financial.dao.refund.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class RefundBillsBaseDO {
    protected Integer id;

    protected String outTradeNo;

    protected String outRefundNo;

    protected Short refundType;

    protected Short businessType;

    protected String businessId;

    protected Short refundStatus;

    protected BigDecimal totalFee;

    protected String onlineTradeNo;

    protected Date refundTime;

    protected Date createTime;

    protected Date updateTime;

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

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo == null ? null : outRefundNo.trim();
    }

    public Short getRefundType() {
        return refundType;
    }

    public void setRefundType(Short refundType) {
        this.refundType = refundType;
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

    public Short getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Short refundStatus) {
        this.refundStatus = refundStatus;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getOnlineTradeNo() {
        return onlineTradeNo;
    }

    public void setOnlineTradeNo(String onlineTradeNo) {
        this.onlineTradeNo = onlineTradeNo == null ? null : onlineTradeNo.trim();
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
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
}