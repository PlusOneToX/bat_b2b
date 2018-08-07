package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorOneListDO {
    private Integer id;

    private String name;

    private String password;

    private Short companyType;

    private String companyName;

    private Short applyType;

    private Short applyStatus;

    private Short profileStatus;

    private Date applyTime;

    private Date checkTime;

    private Short freezeStatus;

    private Date freezeTime;

    private Integer treeNode;

    private Date createTime;

    private Date updateTime;

    private Short autoDelivery;
}