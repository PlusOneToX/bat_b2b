package com.bat.thirdparty.erp.api.request;


public class AccountBalanceChangeRequest {

    private Long erpDistributorId;//分销商内码
    private Short changeType;//变更类型 1.增加，2.减少
    private Double amount;//变动金额
    private Long businessNo;
    private String billNo;//erp单据编号

    public Long getErpDistributorId() {
        return erpDistributorId;
    }

    public void setErpDistributorId(Long erpDistributorId) {
        this.erpDistributorId = erpDistributorId;
    }

    public Short getChangeType() {
        return changeType;
    }

    public void setChangeType(Short changeType) {
        this.changeType = changeType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(Long businessNo) {
        this.businessNo = businessNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
