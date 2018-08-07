package com.bat.goods.dao.attribute.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class AttributeDO {
    private Integer id;
    private String name;
    private String nameEn;
    private String description;
    private Short openFlag;
    private Short attributeType;
    private Date createTime;
    private Date updateTime;
}
