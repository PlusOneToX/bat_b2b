package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderPayStatusCmd implements Serializable {

    /**
     * 订单类型 1分销商 2 C端客户
     */
    private Short receiverType;

    /**
     * 分销商(订单类型为1 时有值)
     */
    private Integer distributorId;
    /**
     * C端客户（订单类型为2 时有值）
     */
    private Integer customerId;

    private Integer orderId;
    /**
     * 付款状态 1.未付款，2.部分付款 3. 已付款 4.部分已退款 5.退款申请中 6.已退款
     */
    private Short payStatus;
    /**
     * 退款金额（付款状态为4或6时需传值）
     */
    private BigDecimal refundedAmount;

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Short getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Short receiverType) {
        this.receiverType = receiverType;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "OrderPayStatusCmd{" +
                "receiverType=" + receiverType +
                ", distributorId=" + distributorId +
                ", customerId=" + customerId +
                ", orderId=" + orderId +
                ", payStatus=" + payStatus +
                '}';
    }
}
