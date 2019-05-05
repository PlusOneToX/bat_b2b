package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;


public class DistributorNewOrderMsgDTO implements Serializable {

    private Integer orderId;

    private Integer distributorId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }
}
