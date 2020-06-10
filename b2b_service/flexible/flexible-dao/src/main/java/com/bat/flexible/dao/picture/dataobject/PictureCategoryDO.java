package com.bat.flexible.dao.picture.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PictureCategoryDO implements Serializable {
    private static final long serialVersionUID = -680085910539697454L;
    private Integer id;

    private Integer parentId;

    private String name;

    private String englishName;

    private BigDecimal sequence;

    private String image;

    private Short atLastTrademark;

    private Short openFlag;

    private Short type;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short delFlag;

    private String description;


}