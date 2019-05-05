package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

import lombok.Data;

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