package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExchangeCodeDTORpcQry implements Serializable {
    private static final long serialVersionUID = 3854333954754943912L;
    private Integer id;

    private Integer exchangeId;

    private String plainCode;

    private String secretCode;

    private String boxCode;

    private Short status;

    private Integer distributorOrderId;

    private Integer distributorOrderGoodsId;

    private Integer distributorId;

    private String distributorName;

    private Long userId;

    private String userName;

    private Integer userOrderId;

    private Integer userOrderGoodsId;

    private String userThirdOrderNo;

    private Date useTime;


    private Integer exchangeFactoryId;

    private String outboundNo;

    private String distributorCompanyName;

}