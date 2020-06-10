package com.bat.flexible.dao.picture.dataobject;

import lombok.Data;

import java.util.Date;
@Data
public class PictureMaterialRelevanceDO {
    private Integer id;

    private Integer pictureId;

    private Integer materialId;

    private Short delFlag;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


}