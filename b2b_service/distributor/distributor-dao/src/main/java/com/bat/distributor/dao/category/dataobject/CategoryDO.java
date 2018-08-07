package com.bat.distributor.dao.category.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CategoryDO {
    private Integer id;

    private String name;

    private String description;

    private String erpCategoryNo;

    private Integer orderTypeId;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}