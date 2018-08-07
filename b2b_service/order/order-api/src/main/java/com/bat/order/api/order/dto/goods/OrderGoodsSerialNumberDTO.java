package com.bat.order.api.order.dto.goods;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsSerialNumberDTO implements Serializable {

    /**
     * B2B订单号
     */
    private String orderNo;


    private Integer serialNumber;

    private String itemCode;
}
