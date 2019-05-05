package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PlatformTenantDiyFactoryDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private String factoryNo;

    private String factoryName;

    private String appId;

    private String appKey;

    private String appSecret;

    private String orderAddUrl;

    private String orderCancelUrl;

    private String supplierNo;

    private String warehouseNo;

    private String shopName;

    private Date createTime;

    private Date updateTime;

    private String configOther;
}