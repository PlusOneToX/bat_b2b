package com.bat.distributor.dao.platform.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PlatformDO {
    private Integer id;

    private String platformNo;

    private String name;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

}