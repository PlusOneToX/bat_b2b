package com.bat.dubboapi.order.order.dto.data;

import java.io.Serializable;

public class OrderGoodsThirdCodeRpcDTO implements Serializable {
    private Integer id;

    private Integer orderGoodsId;

    private Integer orderId;

    private String code;

    private Short writeOffFlag;

    private Integer distributorId;

    private String platform;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(Integer orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Short getWriteOffFlag() {
        return writeOffFlag;
    }

    public void setWriteOffFlag(Short writeOffFlag) {
        this.writeOffFlag = writeOffFlag;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}