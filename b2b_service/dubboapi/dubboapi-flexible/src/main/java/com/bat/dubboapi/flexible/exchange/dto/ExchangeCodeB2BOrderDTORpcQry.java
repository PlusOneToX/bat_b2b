package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExchangeCodeB2BOrderDTORpcQry implements Serializable {

    private static final long serialVersionUID = 4811889487484661956L;
    //兑换码id
    private Integer id;

    //暗码 已解密
    private String secretCode;

    //盒码
    private String boxCode;

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    private Short status;

    //分销商下单
    private Integer distributorOrderId;

    //分销商下单明细
    private Integer distributorOrderGoodsId;

    private Integer distributorId;

    private String distributorName;

    private String distributorCompanyName;

    //兑换卡活动id
    private Integer exchangeId;

    private BigDecimal distributorQuanyiPrice;

    private Short exchangeWay;

}