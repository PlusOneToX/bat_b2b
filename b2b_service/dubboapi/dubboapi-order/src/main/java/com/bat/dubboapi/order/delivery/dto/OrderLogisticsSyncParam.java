package com.bat.dubboapi.order.delivery.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderLogisticsSyncParam implements Serializable {
    private static final long serialVersionUID = 7767577241990740635L;


    private Integer orderId;

    private String orderNo;

    private Integer distributionId;

    private String distributionName;

    private String platform;

    private String otherOrderNo;
    private Integer distributorId;
    private String userId;
    // @JsonIgnore
    private Long id;
    //物流信息
    private String expressNo;
    private String expressCode;
    private String expressName;

    private Long expressTime;

}
