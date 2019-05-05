package com.bat.dubboapi.order.delivery.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsDetailCountDTO implements Serializable {


    private static final long serialVersionUID = 7849763728949496929L;

    private String itemCode;


    private Integer count;
}
