package com.bat.warehouse.dao.stockChangeLog.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class WarehouseStockChangeLogDO {
    private Integer id;

    private Short changeType;

    private Short source;

    private String billNo;

    private String itemCode;

    private String warehouseNo;

    private Integer num;

    private Date createTime;

    private Date updateTime;


}