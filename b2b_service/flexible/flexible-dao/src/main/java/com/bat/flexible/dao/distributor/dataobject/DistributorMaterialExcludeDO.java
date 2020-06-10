package com.bat.flexible.dao.distributor.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DistributorMaterialExcludeDO implements Serializable {
    private static final long serialVersionUID = -2274991490077370897L;
    private Integer id;

    private Integer materialId;

    private Integer distributorId;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short delFlag;

   
}