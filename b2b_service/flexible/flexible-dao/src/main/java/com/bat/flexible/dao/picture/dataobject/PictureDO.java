package com.bat.flexible.dao.picture.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PictureDO {
    private Integer id;

    private String code;

    private String name;

    private String englishName;

    private Short type;

    private Integer categoryId;

    private Integer sequence;

    private String originImage;

    private String thumbnail;

    private Short openFlag;

    private Short delFlag;

    private Short modelScope;

    private Short resellerScope;

    private String themeUrl;

    private BigDecimal templateCenterX;

    private BigDecimal templateCenterY;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private BigDecimal copyrightCost;

    private String description;

    private String backgroundPreviewUrl;

    private String noCameraVacancyPreview;


}