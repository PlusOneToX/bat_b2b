package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExchangeCardDTORpc implements Serializable {
    private static final long serialVersionUID = -4364293378686311600L;
    private Integer id;

    private Integer itemId;

    private String codeName;

    private Short status;

    private String codeDesc;

    private Short type;

    private Short source;

    private Integer codeQuantity;

    private Integer limitQuantity;

    private Integer saleQuantity;

    private Integer exchangeQuantity;

    private Integer refundQuantity;

    private Integer invalidCount;

    private Long startTime;

    private Long endTime;

    private Short exchangeWay;

    private BigDecimal orderUseThreshold;

    private Short goodsScope;

    private String qrCodeUrl;

    private Short isEntity;

    private Short isUseMall;

    private Short mallType;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private String modelNo;

    private String headImg;

    private Short distributorScope;

    private Short mailSetting;

    private BigDecimal mailFee;


}