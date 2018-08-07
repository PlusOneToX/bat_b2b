package com.bat.financial.dao.refund.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class RefundBaseApplyDO {
    protected Integer id;

    protected Integer distributorRefundId;

    protected Short businessTypes;

    protected Integer businessId;

    protected BigDecimal amount;

    protected BigDecimal cashAmount;

    protected BigDecimal depositAmount;

    protected Short refundType;

    protected Short refundMode;

    protected Short applyStatus;

    protected String remark;

    protected Integer operatorId;

    protected String operatorName;

    protected Date createTime;

    protected Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorRefundId() {
        return distributorRefundId;
    }

    public void setDistributorRefundId(Integer distributorRefundId) {
        this.distributorRefundId = distributorRefundId;
    }

    public Short getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(Short businessTypes) {
        this.businessTypes = businessTypes;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Short getRefundType() {
        return refundType;
    }

    public void setRefundType(Short refundType) {
        this.refundType = refundType;
    }

    public Short getRefundMode() {
        return refundMode;
    }

    public void setRefundMode(Short refundMode) {
        this.refundMode = refundMode;
    }

    public Short getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Short applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
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