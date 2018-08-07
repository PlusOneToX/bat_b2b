package com.bat.financial.dao.refund.dataobject;

public class RefundCustomerApplyDO extends RefundBaseApplyDO {
    protected Integer customerId;

    protected String customerName;

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
        return "RefundCustomerApplyDO{" + "customerId=" + customerId + ", customerName='" + customerName + '\''
            + ", id=" + id + ", distributorRefundId=" + distributorRefundId + ", businessTypes=" + businessTypes
            + ", businessId=" + businessId + ", amount=" + amount + ", cashAmount=" + cashAmount + ", depositAmount="
            + depositAmount + ", refundType=" + refundType + ", refundMode=" + refundMode + ", applyStatus="
            + applyStatus + ", remark='" + remark + '\'' + ", operatorId=" + operatorId + ", operatorName='"
            + operatorName + '\'' + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}