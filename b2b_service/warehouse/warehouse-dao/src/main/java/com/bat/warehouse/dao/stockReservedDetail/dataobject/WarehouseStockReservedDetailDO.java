package com.bat.warehouse.dao.stockReservedDetail.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class WarehouseStockReservedDetailDO {
    private Integer id;

    private Integer goodsId;

    private Integer itemId;

    private String itemName;

    private Integer numReserved;

    private Integer reservedId;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


}