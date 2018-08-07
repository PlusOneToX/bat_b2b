package com.bat.financial.dao.deposit.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class WithdrawDepositsDistributorApplyDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private BigDecimal withdrawAmount;

    private Short applyStatus;

    private Short withdrawAccountType;

    private String wxAlipayAccount;

    private String cardNo;

    private String payee;

    private String bankName;

    private String remark;

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

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Short getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Short applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Short getWithdrawAccountType() {
        return withdrawAccountType;
    }

    public void setWithdrawAccountType(Short withdrawAccountType) {
        this.withdrawAccountType = withdrawAccountType;
    }

    public String getWxAlipayAccount() {
        return wxAlipayAccount;
    }

    public void setWxAlipayAccount(String wxAlipayAccount) {
        this.wxAlipayAccount = wxAlipayAccount == null ? null : wxAlipayAccount.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee == null ? null : payee.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}