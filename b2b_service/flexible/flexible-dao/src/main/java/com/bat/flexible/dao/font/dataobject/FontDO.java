package com.bat.flexible.dao.font.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FontDO implements Serializable {
    private static final long serialVersionUID = -951421653071539051L;
    private Integer id;

    private String name;

    private String fileName;

    private String englishName;

    private Integer sequence;

    private String fontFile;

    private String description;

    private Short openFlag;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short delFlag;


}