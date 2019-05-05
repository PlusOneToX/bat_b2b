package com.bat.dubboapi.order.order.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderCostRpcDTO implements Serializable {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 余额支付金额
     */
    private BigDecimal depositAmount;
    /**
     * 应付款总额
     */
    private BigDecimal payAmount;
    /**
     * 已付款金额
     */
    private BigDecimal paidAmount;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
}
