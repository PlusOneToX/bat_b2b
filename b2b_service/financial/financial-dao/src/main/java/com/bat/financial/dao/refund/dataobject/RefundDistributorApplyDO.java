package com.bat.financial.dao.refund.dataobject;

public class RefundDistributorApplyDO extends RefundBaseApplyDO {

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
        return "RefundDistributorApplyDO{" + "distributorId=" + distributorId + ", distributorName='" + distributorName
            + '\'' + ", id=" + id + ", distributorRefundId=" + distributorRefundId + ", businessTypes=" + businessTypes
            + ", businessId=" + businessId + ", amount=" + amount + ", cashAmount=" + cashAmount + ", depositAmount="
            + depositAmount + ", refundType=" + refundType + ", refundMode=" + refundMode + ", applyStatus="
            + applyStatus + ", remark='" + remark + '\'' + ", operatorId=" + operatorId + ", operatorName='"
            + operatorName + '\'' + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}