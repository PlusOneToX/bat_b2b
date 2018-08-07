package com.bat.order.dao.order.dataobject;

public class OrderGoodsCodeDO {
    private Integer id;

    private Integer orderGoodsId;

    private String secretCode;

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

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode == null ? null : secretCode.trim();
    }
}