package com.bat.financial.dao.pay.dataobject;

public class PayBillsCustomerDO extends PayBillsDO {

    private Integer customerId;

    private String customerName;


    private String mchid;

    private String spMchid;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getSpMchid() {
        return spMchid;
    }

    public void setSpMchid(String spMchid) {
        this.spMchid = spMchid;
    }

    @Override
    public String toString() {
        return "PayBillsCustomerDO{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", mchid='" + mchid + '\'' +
                ", spMchid='" + spMchid + '\'' +
                ", id=" + id +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", payType=" + payType +
                ", businessType=" + businessType +
                ", businessId='" + businessId + '\'' +
                ", payStatus=" + payStatus +
                ", totalFee=" + totalFee +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderDescribe='" + orderDescribe + '\'' +
                ", productId='" + productId + '\'' +
                ", onlineTradeNo='" + onlineTradeNo + '\'' +
                ", expireTime=" + expireTime +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", payMethod='" + payMethod + '\'' +
                ", tradeMode=" + tradeMode +
                ", payeeId=" + payeeId +
                ", organizationId=" + organizationId +
                ", appId='" + appId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}