package com.bat.order.dao.stock.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderGoodsStockDO2 {
    private Integer id;

    private Integer orderId;

    private String orderErpNo;

    private Integer orderGoodsId;

    private Integer serialNumber;

    private Integer goodsId;

    private Integer itemId;

    private Integer stockId;

    private Integer warehouseId;

    private Integer lockNum;

    private Short lockType;

    private Date createTime;

    private Date updateTime;
}