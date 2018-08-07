package com.bat.order.api.order.dto.common;

public class OrderGoodsThirdCodeDTO {
    private Integer id;

    private Integer orderGoodsId;

    private Integer orderId;

    private String code;

    private Short writeOffFlag;

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
}