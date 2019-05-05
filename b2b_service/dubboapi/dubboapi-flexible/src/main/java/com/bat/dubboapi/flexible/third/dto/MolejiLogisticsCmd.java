package com.bat.dubboapi.flexible.third.dto;

import java.io.Serializable;

/**
 * 摩乐吉物流参数
 */
public class MolejiLogisticsCmd implements Serializable {

    /**
     * 时间戳（毫秒）
     */
    private Long timestamp;

    /**
     * 工厂ID(摩乐吉定义bat)
     */
    private String factoryId;

    /**
     * 签名
     */
    private String signature;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 快递公司名称
     */
    private String shipperName;

    /**
     * 快递公司编号
     */
    private String shipperCode;

    /**
     * 快递单号
     */
    private String trackingNo;


    /**
     * 发货时间（时间戳、毫秒）
     */
    private Long deliveryTime;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
