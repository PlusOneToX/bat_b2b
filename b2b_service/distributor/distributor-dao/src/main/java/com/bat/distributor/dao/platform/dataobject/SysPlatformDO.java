package com.bat.distributor.dao.platform.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class SysPlatformDO {
    private Integer id;

    private String platform;

    private String appId;

    private String appKey;

    private String appSecret;

    private String hostName;

    private Date createTime;

    private Date updateTime;

}