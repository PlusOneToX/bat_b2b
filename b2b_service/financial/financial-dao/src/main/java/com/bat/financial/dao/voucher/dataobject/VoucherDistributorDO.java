package com.bat.financial.dao.voucher.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class VoucherDistributorDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String companyName;

    private BigDecimal amount;

    private Short payWay;

    private String outTradeNo;

    private Short customerFlag;

    private String currencyType;

    private Short businessType;

    private String businessId;

    private Short voucherStatus;

    private String remark;

    private String voucherErpNo;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Short getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Short customerFlag) {
        this.customerFlag = customerFlag;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
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

    public Short getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(Short voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getVoucherErpNo() {
        return voucherErpNo;
    }

    public void setVoucherErpNo(String voucherErpNo) {
        this.voucherErpNo = voucherErpNo == null ? null : voucherErpNo.trim();
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

    @Override
    public String toString() {
        return "VoucherDistributorDO{" + "id=" + id + ", distributorId=" + distributorId + ", distributorName='"
            + distributorName + '\'' + ", companyName='" + companyName + '\'' + ", amount=" + amount + ", payWay="
            + payWay + ", payBillsId=" + outTradeNo + ", customerFlag=" + customerFlag + ", currencyType='"
            + currencyType + '\'' + ", businessType=" + businessType + ", businessId=" + businessId + ", voucherStatus="
            + voucherStatus + ", remark='" + remark + '\'' + ", voucherErpNo='" + voucherErpNo + '\'' + ", createTime="
            + createTime + ", updateTime=" + updateTime + '}';
    }
}