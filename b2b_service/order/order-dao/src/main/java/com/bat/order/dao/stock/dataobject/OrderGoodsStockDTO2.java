package com.bat.order.dao.stock.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class OrderGoodsStockDTO2 {

    private Integer orderGoodsId;

    private Integer orderId;

    private String orderNo;

    private String orderErpNo;


    private Short orderStatus;

    private Integer inStockLockNum;

    private Integer onWayLockNum;

    private Integer vmiLockNum;


    private Date createTime;


}