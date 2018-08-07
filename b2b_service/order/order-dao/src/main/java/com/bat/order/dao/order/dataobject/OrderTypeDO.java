package com.bat.order.dao.order.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderTypeDO {
    private Integer id;

    private String name;

    private String erpType;

    private Short specialFlag;

    private Integer warehouseId;

    private String description;

    private Short editable;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

}