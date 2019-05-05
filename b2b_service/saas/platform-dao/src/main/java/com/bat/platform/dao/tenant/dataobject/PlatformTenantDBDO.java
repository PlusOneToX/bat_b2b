package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PlatformTenantDBDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private Short modelType;

    private Short dbType;
    private Short sourceType;
    private Short tableFlag;

    private String dbName;
    private String dbBaseUrl;
    private String dbUrl;

    private String host;

    private String port;

    private String nosqlDatabase;

    private String userName;

    private String password;

    private Date createTime;

    private Date updateTime;
}