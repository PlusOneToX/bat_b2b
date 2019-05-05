package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PlatformTenantErpDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private Short erpType;

    private String baseUrl;

    private String baseExtUrl;

    private String userName;

    private String password;

    private String dbId;

    private String lang;

    private String platform;

    private Integer tokenValidTime;

    private String wlfItemNo;

    private String vmiWarehouse;

    private String poInboundType;

    private String settleDefault;

    private String settleCashOnline;

    private String settleCashOffline;

    private String settleMonth;

    private Date createTime;

    private Date updateTime;
}