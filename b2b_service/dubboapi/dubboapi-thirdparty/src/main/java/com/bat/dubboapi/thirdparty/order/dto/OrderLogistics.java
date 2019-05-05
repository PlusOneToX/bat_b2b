package com.bat.dubboapi.thirdparty.order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderLogistics implements Serializable {
    private static final long serialVersionUID = 6189737059494310999L;
    //订单信息
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
