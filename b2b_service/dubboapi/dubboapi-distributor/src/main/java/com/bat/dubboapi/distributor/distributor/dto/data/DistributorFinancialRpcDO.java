package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

public class DistributorFinancialRpcDO implements Serializable {

    /**
     * 结算方式类型，1为立即支付，2为期间结算
     */
    private Short payWay;
    /**
     * ERP余额是否同步 1.是 0.否
     */
    private Short erpBalanceFlag;
    /**
     * erp结算方式编码
     */
    private String erpSettleAccountNo;
    private Short coinType;
    private String bankAccountName;
    private String bankDepositName;
    private String bankAccount;
    private Short taxType;
    private String invoiceTitleName;
    private String taxpayerNumber;
    private String registeredAddress;
    private String registeredPhone;
    private String registeredBankDepositName;
    private String registeredBankAccount;

    public Short getErpBalanceFlag() {
        return erpBalanceFlag;
    }

    public void setErpBalanceFlag(Short erpBalanceFlag) {
        this.erpBalanceFlag = erpBalanceFlag;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getErpSettleAccountNo() {
        return erpSettleAccountNo;
    }

    public void setErpSettleAccountNo(String erpSettleAccountNo) {
        this.erpSettleAccountNo = erpSettleAccountNo;
    }

    public Short getCoinType() {
        return coinType;
    }

    public void setCoinType(Short coinType) {
        this.coinType = coinType;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankDepositName() {
        return bankDepositName;
    }

    public void setBankDepositName(String bankDepositName) {
        this.bankDepositName = bankDepositName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Short getTaxType() {
        return taxType;
    }

    public void setTaxType(Short taxType) {
        this.taxType = taxType;
    }

    public String getInvoiceTitleName() {
        return invoiceTitleName;
    }

    public void setInvoiceTitleName(String invoiceTitleName) {
        this.invoiceTitleName = invoiceTitleName;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getRegisteredPhone() {
        return registeredPhone;
    }

    public void setRegisteredPhone(String registeredPhone) {
        this.registeredPhone = registeredPhone;
    }

    public String getRegisteredBankDepositName() {
        return registeredBankDepositName;
    }

    public void setRegisteredBankDepositName(String registeredBankDepositName) {
        this.registeredBankDepositName = registeredBankDepositName;
    }

    public String getRegisteredBankAccount() {
        return registeredBankAccount;
    }

    public void setRegisteredBankAccount(String registeredBankAccount) {
        this.registeredBankAccount = registeredBankAccount;
    }
}