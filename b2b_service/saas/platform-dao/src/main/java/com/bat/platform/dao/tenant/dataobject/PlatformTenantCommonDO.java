package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PlatformTenantCommonDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private String wxProgramAppSecret;

    private String wxProgramAppId;

    private Integer exchangeDistributorId;

    private Date createTime;

    private Date updateTime;

    private String colour;
}