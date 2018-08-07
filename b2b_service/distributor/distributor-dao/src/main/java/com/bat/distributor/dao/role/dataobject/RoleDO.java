package com.bat.distributor.dao.role.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class RoleDO {
    private Integer id;

    private String name;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}