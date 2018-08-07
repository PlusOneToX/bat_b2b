package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorCheckDO {
    private Integer id;

    private Integer distributorId;

    private Short checkStatus;

    private Short checkType;

    private String checkContent;

    private Integer userId;

    private String userName;

    private Integer checkUserId;

    private String checkUserName;

    private Date createTime;

    private Date updateTime;

}