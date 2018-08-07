package com.bat.order.dao.order.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class OrderGoodsVmiDO {

    private Integer id;

    private Integer orderId;

    private Integer itemId;

    private String itemCode;

    private String itemName;

    private Integer itemVmiCount;

    private String orderErpNo;

    private Date orderCreateTime;

    private Short payStatus;

    private Short orderStatus;
}