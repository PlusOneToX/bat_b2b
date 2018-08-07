package com.bat.financial.dao.refund.dataobject;

public class RefundBillsCustomerDO extends RefundBillsBaseDO {
    private Integer customerId;

    private String customerName;

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

    @Override
    public String toString() {
        return "RefundBillsCustomerDO{" + "customerId=" + customerId + ", customerName='" + customerName + '\''
            + ", id=" + id + ", outTradeNo='" + outTradeNo + '\'' + ", outRefundNo='" + outRefundNo + '\''
            + ", refundType=" + refundType + ", businessType=" + businessType + ", businessId=" + businessId
            + ", refundStatus=" + refundStatus + ", totalFee=" + totalFee + ", onlineTradeNo='" + onlineTradeNo + '\''
            + ", refundTime=" + refundTime + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}