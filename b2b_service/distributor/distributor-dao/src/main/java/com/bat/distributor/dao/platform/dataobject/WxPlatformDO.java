package com.bat.distributor.dao.platform.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class WxPlatformDO {
    private Integer id;

    private String platform;

    private Short type;

    private String name;

    private String appId;

    private String appSecret;

    private Date createTime;

    private Date updateTime;

}