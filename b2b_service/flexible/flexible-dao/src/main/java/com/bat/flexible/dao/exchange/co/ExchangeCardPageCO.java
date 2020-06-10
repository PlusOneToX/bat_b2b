package com.bat.flexible.dao.exchange.co;


import lombok.Data;

import java.io.Serializable;

@Data
public class ExchangeCardPageCO implements Serializable {
    private static final long serialVersionUID = 7164697521759735229L;


    private Integer id;

    private String codeName;

    private Short status;

    private Short type;

    private Short source;

    private Integer codeQuantity;

    private Integer exchangeQuantity;

    private Integer saleQuantity;

    private Long startTime;
    private Long endTime;
    private Short exchangeWay;


    private String qrCodeUrl;


    private Integer synchronizedQuantity;

    //是否可以同步工厂 1、是 0、否
    private Byte isSync;

    //是否使用兑换商城 1、是 0、否
    private Short isUseMall;

    private Short isEntity;

    private Short mailSetting;


}
