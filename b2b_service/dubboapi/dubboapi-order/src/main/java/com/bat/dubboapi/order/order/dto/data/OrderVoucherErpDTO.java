package com.bat.dubboapi.order.order.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderVoucherErpDTO implements Serializable {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * erp订单编号
     */
    private String orderErpNo;
    /**
     * 订单实际付款金额
     */
    private BigDecimal paidAmount;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderErpNo() {
        return orderErpNo;
    }

    public void setOrderErpNo(String orderErpNo) {
        this.orderErpNo = orderErpNo;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Override
    public String toString() {
        return "OrderVoucherErpDTO{" + "orderId=" + orderId + ", orderErpNo='" + orderErpNo + '\'' + ", paidAmount="
            + paidAmount + '}';
    }
}