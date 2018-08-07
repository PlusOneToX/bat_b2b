package com.bat.order.dao.order.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class OrderGoodsExchangeCodeListDO {


    private Integer id;


    private Integer distributorId;


    private String distributorName;


    private Integer orderId;


    private String orderNo;


    private String userName;


    private String mobile;


    private String secretCode;


    private Integer exchangeOrderId;

    private Short orderStatus;

    private Short payWay;

    private Date createTime;
}
