package com.bat.flexible.dao.distributor.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FlexibleDistributorCooperationDO implements Serializable {
    private static final long serialVersionUID = 7376158344133760684L;
    private Integer id;

    private Integer distributorId;

    private Integer sequence;

    private Short defaultChoose;

    private Short cooperationType;

    private String remark;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short openFlag;

    private Short delFlag;

  
}