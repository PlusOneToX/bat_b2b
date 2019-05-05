package com.bat.gateway.filter;

import lombok.Data;

import java.util.Date;

@Data
public class PlatformTenantUrlDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private Short urlType;

    private String host;

    private String url;

    private Date createTime;

    private Date updateTime;
}