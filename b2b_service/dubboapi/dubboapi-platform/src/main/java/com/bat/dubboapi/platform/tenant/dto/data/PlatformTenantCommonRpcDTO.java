package com.bat.dubboapi.platform.tenant.dto.data;

import java.io.Serializable;
import java.util.Date;

public class PlatformTenantCommonRpcDTO implements Serializable {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private String wxProgramAppSecret;

    private String wxProgramAppId;

    private Integer exchangeDistributorId;

    private Date createTime;

    private Date updateTime;

    public Integer getExchangeDistributorId() {
        return exchangeDistributorId;
    }

    public void setExchangeDistributorId(Integer exchangeDistributorId) {
        this.exchangeDistributorId = exchangeDistributorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo == null ? null : tenantNo.trim();
    }

    public String getWxProgramAppSecret() {
        return wxProgramAppSecret;
    }

    public void setWxProgramAppSecret(String wxProgramAppSecret) {
        this.wxProgramAppSecret = wxProgramAppSecret == null ? null : wxProgramAppSecret.trim();
    }

    public String getWxProgramAppId() {
        return wxProgramAppId;
    }

    public void setWxProgramAppId(String wxProgramAppId) {
        this.wxProgramAppId = wxProgramAppId == null ? null : wxProgramAppId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}