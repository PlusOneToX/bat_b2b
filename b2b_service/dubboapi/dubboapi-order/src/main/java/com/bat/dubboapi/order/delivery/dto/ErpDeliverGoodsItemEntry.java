package com.bat.dubboapi.order.delivery.dto;

import java.io.Serializable;

/**
 * Created by apple on 2019/7/11.
 */
public class ErpDeliverGoodsItemEntry implements Serializable {
    private static final long serialVersionUID = -8205895460603783038L;
    private String orderNo;
    private String deliverId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }


}
