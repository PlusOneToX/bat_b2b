package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class DistributorContactsDO {
    private Integer id;

    private Integer distributorId;

    private String name;

    private String password;

    private Short sex;

    private String phone;

    private String email;

    private Short ownerFlag;

    private Integer roleId;

    private Short freezeStatus;

    private Date createTime;

    private Date updateTime;

    private String openId;
}