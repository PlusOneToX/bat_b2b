package com.bat.warehouse.dao.stockReserved.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class WarehouseStockReservedDO {
    private Integer id;

    private Integer warehouseId;


    private String remark;

    private Integer businessId;

    private Short source;

    private Short status;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


   
}