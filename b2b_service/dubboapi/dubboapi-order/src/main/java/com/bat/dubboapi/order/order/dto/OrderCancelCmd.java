package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;

public class OrderCancelCmd implements Serializable {
    /**
     * 订单Id
     */
    Integer orderId;
    /**
     * 订单编号
     */
    String orderNo;
    /**
     * 第三方系统订单编号
     */
    String orderThirdpartyNo;
    /**
     * 分销商id
     */
    Integer distributorId;
    /**
     * erp订单编号
     */
    String orderErpNo;
    /**
     * 取消订单备注
     */
    String remark;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderThirdpartyNo() {
        return orderThirdpartyNo;
    }

    public void setOrderThirdpartyNo(String orderThirdpartyNo) {
        this.orderThirdpartyNo = orderThirdpartyNo;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrderErpNo() {
        return orderErpNo;
    }

    public void setOrderErpNo(String orderErpNo) {
        this.orderErpNo = orderErpNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}