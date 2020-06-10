package com.bat.flexible.dao.exchange.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExchangeCodeDO {
    private Integer id;

    private Integer exchangeId;

    private String plainCode;

    private String secretCode;

    private String boxCode;

    private Short status;

    private Integer distributorOrderId;

    private BigDecimal distributorQuanyiPrice;

    private Integer distributorOrderGoodsId;

    private Integer distributorId;

    private String distributorName;

    private Long userId;

    private String userName;

    private Integer userOrderId;

    private Integer userOrderGoodsId;

    private String userThirdOrderNo;

    private Date useTime;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private Integer exchangeFactoryId;

    private String outboundNo;

    private String distributorCompanyName;

}