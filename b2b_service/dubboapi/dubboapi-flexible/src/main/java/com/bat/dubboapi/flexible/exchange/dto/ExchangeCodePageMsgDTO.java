package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExchangeCodePageMsgDTO implements Serializable {

    private static final long serialVersionUID = -5730077192452522638L;
    /**
     * 活动名称
     */
    private String codeName;

    private Integer exchangeId;

    private String plainCode;


    private Integer exchangeFactoryId;


    private String boxCode;


    private String secretCode;

}
