package com.bat.financial.dao.refund.dataobject;

public class RefundBillsDistributorDO extends RefundBillsBaseDO {
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
        return "RefundBillsDistributorDO{" + "distributorId=" + distributorId + ", distributorName='" + distributorName
            + '\'' + ", id=" + id + ", outTradeNo='" + outTradeNo + '\'' + ", outRefundNo='" + outRefundNo + '\''
            + ", refundType=" + refundType + ", businessType=" + businessType + ", businessId=" + businessId
            + ", refundStatus=" + refundStatus + ", totalFee=" + totalFee + ", onlineTradeNo='" + onlineTradeNo + '\''
            + ", refundTime=" + refundTime + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}