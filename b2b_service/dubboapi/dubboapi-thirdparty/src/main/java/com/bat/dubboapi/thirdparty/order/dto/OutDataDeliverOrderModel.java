package com.bat.dubboapi.thirdparty.order.dto;

import java.io.Serializable;

public class OutDataDeliverOrderModel implements Serializable {

    private static final long serialVersionUID = 1858386591078447074L;
    private String deliverNo;

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }
}
