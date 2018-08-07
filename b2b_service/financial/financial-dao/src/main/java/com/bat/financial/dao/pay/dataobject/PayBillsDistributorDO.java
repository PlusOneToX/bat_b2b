package com.bat.financial.dao.pay.dataobject;

public class PayBillsDistributorDO extends PayBillsDO {

    private Integer distributorId;

    private String distributorName;

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

    @Override
    public String toString() {
        return "PayBillsDistributorDO{" + "distributorId=" + distributorId + ", distributorName='" + distributorName
            + '\'' + ", id=" + id + ", outTradeNo='" + outTradeNo + '\'' + ", payType=" + payType + ", businessType="
            + businessType + ", businessId='" + businessId + '\'' + ", payStatus=" + payStatus + ", totalFee="
            + totalFee + ", orderTitle='" + orderTitle + '\'' + ", orderDescribe='" + orderDescribe + '\''
            + ", productId='" + productId + '\'' + ", onlineTradeNo='" + onlineTradeNo + '\'' + ", expireTime="
            + expireTime + ", payTime=" + payTime + ", createTime=" + createTime + ", updateTime=" + updateTime
            + ", payMethod='" + payMethod + '\'' + ", tradeMode=" + tradeMode + ", payeeId=" + payeeId
            + ", organizationId=" + organizationId + ", currencyCode='" + currencyCode + '\'' + '}';
    }
}