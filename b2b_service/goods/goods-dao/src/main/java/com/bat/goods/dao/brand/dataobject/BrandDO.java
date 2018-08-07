package com.bat.goods.dao.brand.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class BrandDO implements Serializable {
    private Integer id;
    private String name;
    private String nameEn;
    private String logo;
    private String description;
    private Short openFlag;
    private Integer sort;
    private Short distributorScope;
    private Short distributorScopeNo;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
