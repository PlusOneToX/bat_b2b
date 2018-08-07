package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorNextListDO {
    private Integer id;
    private String name;
    private String companyName;
    private String userName;
    private String phone;
    private String email;
    private Short applyStatus;
    private Short profileStatus;
    private Date applyTime;
    private Date checkTime;
    private Short freezeStatus;
    private Date freezeTime;
    private Integer treeNode;
    private Date createTime;
    private Date updateTime;
}