package com.bat.goods.dao.classify.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class ClassifyDO {
    private Integer id;
    private String name;
    private String nameEn;
    private String description;
    private Short openFlag;
    private Integer sort;
    private Integer parentId;
    private String imageUrl;
    private String imageUrlEn;
    private Date createTime;
    private Date updateTime;
    private Short recommendFlag;
    private String appletName;
}
