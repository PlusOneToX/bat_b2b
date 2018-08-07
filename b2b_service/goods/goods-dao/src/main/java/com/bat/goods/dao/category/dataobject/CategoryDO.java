package com.bat.goods.dao.category.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CategoryDO {
    private Integer id;
    private String name;
    private String nameEn;
    private String thirdpartyId;
    private String description;
    private Short openFlag;
    private Integer sort;
    private Short distributorScope;
    private Short distributorScopeNo;
    private Date createTime;
    private Date updateTime;
}
