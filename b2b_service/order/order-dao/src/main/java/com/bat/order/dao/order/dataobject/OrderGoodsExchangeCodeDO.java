package com.bat.order.dao.order.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderGoodsExchangeCodeDO {
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