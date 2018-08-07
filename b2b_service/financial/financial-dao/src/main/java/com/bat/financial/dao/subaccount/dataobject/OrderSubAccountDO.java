package com.bat.financial.dao.subaccount.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSubAccountDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private Integer orderId;

    private String orderNo;

    private String transactionId;

    private String lastTransactionId;

    private Integer shopId;

    private String shopName;

    private BigDecimal payAmount;

    private BigDecimal maxSubAccountAmount;

    private BigDecimal profitAccount;

    private BigDecimal actualSubAccountAmount;

    private Short status;

    private String subMchid;

    private Short subAccountFailFlag;

    private Date createTime;

    private Date updateTime;

    private String remark;

    private Date planTime;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getMaxSubAccountAmount() {
        return maxSubAccountAmount;
    }

    public void setMaxSubAccountAmount(BigDecimal maxSubAccountAmount) {
        this.maxSubAccountAmount = maxSubAccountAmount;
    }

    public BigDecimal getProfitAccount() {
        return profitAccount;
    }

    public void setProfitAccount(BigDecimal profitAccount) {
        this.profitAccount = profitAccount;
    }

    public BigDecimal getActualSubAccountAmount() {
        return actualSubAccountAmount;
    }

    public void setActualSubAccountAmount(BigDecimal actualSubAccountAmount) {
        this.actualSubAccountAmount = actualSubAccountAmount;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid == null ? null : subMchid.trim();
    }

    public Short getSubAccountFailFlag() {
        return subAccountFailFlag;
    }

    public void setSubAccountFailFlag(Short subAccountFailFlag) {
        this.subAccountFailFlag = subAccountFailFlag;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLastTransactionId() {
        return lastTransactionId;
    }

    public void setLastTransactionId(String lastTransactionId) {
        this.lastTransactionId = lastTransactionId;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }
}