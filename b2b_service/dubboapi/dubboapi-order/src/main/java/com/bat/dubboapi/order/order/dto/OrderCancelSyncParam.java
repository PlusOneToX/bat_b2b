package com.bat.dubboapi.order.order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderCancelSyncParam implements Serializable {

    private Integer orderId;

    private String orderNo;


    private String platform;

    private String otherOrderNo;
    private Integer distributorId;


}
