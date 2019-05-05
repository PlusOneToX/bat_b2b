package com.bat.dubboapi.order.delivery.dto;


import java.io.Serializable;

public class OrderDeliveryCmd implements Serializable {

    private static final long serialVersionUID = 2426612674021046573L;

    /**
     * 是否手动发货
     */
    private Boolean manualFlag = false;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 快递公司编码
     */
    private String expressCode;

    /**
     * 快递公司名称
     */
    private String expressName;

    /**
     * 发货时间(毫秒)
     */
    private Long expressTime;

    /**
     * 订单号
     */
    private String orderNo;


    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public Long getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(Long expressTime) {
        this.expressTime = expressTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }
}
