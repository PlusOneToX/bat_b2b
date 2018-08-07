package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: lim
 * @description: 支付回调
 * @date: 2018/5/31 19:40
 */
public class PayCallBackDTO {

    private Integer distributorId;

    private String distributorName;

    private String companyName;

    private BigDecimal amount;

    private Short payWay;

    private Integer payBillsId;

    private String currencyType;

    private Short businessType;

    private Integer businessId;

    private String remark;

    private String outTradeNo;

    /**
     * 如果支付完成 为支付时间
     */
    private Date payTime;

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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
        this.distributorName = distributorName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Integer getPayBillsId() {
        return payBillsId;
    }

    public void setPayBillsId(Integer payBillsId) {
        this.payBillsId = payBillsId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Short getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Short businessType) {
        this.businessType = businessType;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Override
    public String toString() {
        return "PayCallBackDTO{" + "distributorId=" + distributorId + ", distributorName='" + distributorName + '\''
            + ", companyName='" + companyName + '\'' + ", amount=" + amount + ", payWay=" + payWay + ", payBillsId="
            + payBillsId + ", currencyType='" + currencyType + '\'' + ", businessType=" + businessType + ", businessId="
            + businessId + ", remark='" + remark + '\'' + ", outTradeNo='" + outTradeNo + '\'' + '}';
    }
}
