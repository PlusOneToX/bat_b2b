package com.bat.flexible.dao.picture.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PictureTemplateEditDO {
    private Integer id;

    private Integer pictureId;

    private String internalEditUrl;

    private BigDecimal editCenterX;

    private BigDecimal editCenterY;

    private BigDecimal length;

    private BigDecimal width;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


}