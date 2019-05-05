package com.bat.dubboapi.order.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderGoodsExchangeCodeRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -2053645158749774194L;
    private Integer id;

    private Integer orderId;

    private Integer orderGoodsId;

    private Integer exchangeId;

    private String secretCode;

    private Short mailSetting;

    private BigDecimal mailFee;

    private Integer exchangeOrderId;

    private Integer exchangeOrderGoodsId;

    private Integer distributorId;

    private String distributorCompanyName;

    private Integer salesId;

    private String salesName;

}