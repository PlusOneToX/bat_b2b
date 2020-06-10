package com.bat.flexible.dao.distributor.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DistributorPictureExcludeDO implements Serializable {
    private static final long serialVersionUID = 7721714303662463483L;
    private Integer id;

    private Integer pictureId;

    private Integer distributorId;

    private Short delFlag;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


}