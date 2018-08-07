package com.bat.financial.dao.deposit.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class DepositDistributorSubsidiaryBookDO {
    private Integer id;

    private Integer depositDistributorId;

    private Integer distributorId;

    private String distributorName;

    private Short businessType;

    private Short payWay;

    private String businessId;

    private Short changeType;

    private BigDecimal amount;

    private BigDecimal beforeDepositAmount;

    private BigDecimal afterDepositAmount;

    private String remark;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepositDistributorId() {
        return depositDistributorId;
    }

    public void setDepositDistributorId(Integer depositDistributorId) {
        this.depositDistributorId = depositDistributorId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName == null ? null : distributorName.trim();
    }

    public Short getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Short businessType) {
        this.businessType = businessType;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Short getChangeType() {
        return changeType;
    }

    public void setChangeType(Short changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBeforeDepositAmount() {
        return beforeDepositAmount;
    }

    public void setBeforeDepositAmount(BigDecimal beforeDepositAmount) {
        this.beforeDepositAmount = beforeDepositAmount;
    }

    public BigDecimal getAfterDepositAmount() {
        return afterDepositAmount;
    }

    public void setAfterDepositAmount(BigDecimal afterDepositAmount) {
        this.afterDepositAmount = afterDepositAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DepositDistributorSubsidiaryBookDO{" + "id=" + id + ", depositDistributorId=" + depositDistributorId
            + ", distributorId=" + distributorId + ", distributorName='" + distributorName + '\'' + ", businessType="
            + businessType + ", payWay=" + payWay + ", businessId=" + businessId + ", changeType=" + changeType
            + ", amount=" + amount + ", beforeDepositAmount=" + beforeDepositAmount + ", afterDepositAmount="
            + afterDepositAmount + ", remark='" + remark + '\'' + ", createTime=" + createTime + '}';
    }
}