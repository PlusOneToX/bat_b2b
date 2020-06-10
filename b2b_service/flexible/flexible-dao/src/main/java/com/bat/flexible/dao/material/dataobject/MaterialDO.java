package com.bat.flexible.dao.material.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MaterialDO implements Serializable {
    
    private static final long serialVersionUID = 1099671792460931881L;
    
    private Integer id;

    private Integer parentId;

    private String materialNo;

    private String name;

    private String englishName;

    private Integer categoryId;

    private Integer sequence;

    private String image;

    private String description;

    private Short openFlag;

    private Short delFlag;

    private String manufactor;

    private String colour;

    private Short atLastTrademark;

    private String stuffName;

    private String stuffEnName;

    private String subtitle;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Integer itemId;

    private String itemCode;

    private Short allowUploadFlag;

    private String content;

    private Short mandatoryCoveredBleedFlag;
}