package com.bat.goods.dao.tag.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class TagDO {
    private Integer id;
    private String name;
    private String nameEn;
    private Short openFlag;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
}
