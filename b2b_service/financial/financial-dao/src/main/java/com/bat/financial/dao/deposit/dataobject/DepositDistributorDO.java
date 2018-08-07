package com.bat.financial.dao.deposit.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class DepositDistributorDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String erpDistributorId;

    private Integer treeNode;

    private Integer distributorAncestorId;

    private String distributorAncestorName;

    private BigDecimal accountBalance;

    private BigDecimal accountAvailable;

    private BigDecimal freezingAmount;

    private BigDecimal rechargeAmount;

    private BigDecimal commissionAmount;

    private BigDecimal withdrawAmount;

    private BigDecimal consumerAmount;

    private BigDecimal refundAmount;

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

    public String getErpDistributorId() {
        return erpDistributorId;
    }

    public void setErpDistributorId(String erpDistributorId) {
        this.erpDistributorId = erpDistributorId == null ? null : erpDistributorId.trim();
    }

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }

    public Integer getDistributorAncestorId() {
        return distributorAncestorId;
    }

    public void setDistributorAncestorId(Integer distributorAncestorId) {
        this.distributorAncestorId = distributorAncestorId;
    }

    public String getDistributorAncestorName() {
        return distributorAncestorName;
    }

    public void setDistributorAncestorName(String distributorAncestorName) {
        this.distributorAncestorName = distributorAncestorName == null ? null : distributorAncestorName.trim();
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAccountAvailable() {
        return accountAvailable;
    }

    public void setAccountAvailable(BigDecimal accountAvailable) {
        this.accountAvailable = accountAvailable;
    }

    public BigDecimal getFreezingAmount() {
        return freezingAmount;
    }

    public void setFreezingAmount(BigDecimal freezingAmount) {
        this.freezingAmount = freezingAmount;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getConsumerAmount() {
        return consumerAmount;
    }

    public void setConsumerAmount(BigDecimal consumerAmount) {
        this.consumerAmount = consumerAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
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
        return "DepositDistributorDO{" + "id=" + id + ", distributorId=" + distributorId + ", distributorName='"
            + distributorName + '\'' + ", erpDistributorId='" + erpDistributorId + '\'' + ", treeNode=" + treeNode
            + ", distributorAncestorId=" + distributorAncestorId + ", distributorAncestorName='"
            + distributorAncestorName + '\'' + ", accountBalance=" + accountBalance + ", accountAvailable="
            + accountAvailable + ", freezingAmount=" + freezingAmount + ", rechargeAmount=" + rechargeAmount
            + ", commissionAmount=" + commissionAmount + ", withdrawAmount=" + withdrawAmount + ", consumerAmount="
            + consumerAmount + ", refundAmount=" + refundAmount + ", createTime=" + createTime + ", updateTime="
            + updateTime + '}';
    }
}