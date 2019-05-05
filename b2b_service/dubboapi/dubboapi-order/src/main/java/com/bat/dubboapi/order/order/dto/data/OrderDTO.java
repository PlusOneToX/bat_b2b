package com.bat.dubboapi.order.order.dto.data;

import java.io.Serializable;

public class OrderDTO implements Serializable {
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 订单编号
     */
    private String orderNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}