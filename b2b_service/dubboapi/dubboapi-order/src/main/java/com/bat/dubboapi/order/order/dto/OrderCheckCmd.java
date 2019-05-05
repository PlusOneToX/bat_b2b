package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;

public class OrderCheckCmd implements Serializable {
    /**
     * ERP订单编号
     */
    private String orderErpNo;
    /**
     * 订单状态变更 订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成
     */
    private Short orderStatus;

    public String getOrderErpNo() {
        return orderErpNo;
    }

    public void setOrderErpNo(String orderErpNo) {
        this.orderErpNo = orderErpNo;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }
}