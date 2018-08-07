package com.bat.distributor.dao.salesarea.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class SalesAreaDO {
    private Integer id;

    private String name;

    private String description;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}