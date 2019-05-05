package com.bat.dubboapi.platform.tenant.dto.data;

import java.io.Serializable;
import java.util.Date;

public class PlatformTenantErpRpcDTO implements Serializable {
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

    public String getPoInboundType() {
        return poInboundType;
    }

    public void setPoInboundType(String poInboundType) {
        this.poInboundType = poInboundType;
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

    public Short getErpType() {
        return erpType;
    }

    public void setErpType(Short erpType) {
        this.erpType = erpType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl == null ? null : baseUrl.trim();
    }

    public String getBaseExtUrl() {
        return baseExtUrl;
    }

    public void setBaseExtUrl(String baseExtUrl) {
        this.baseExtUrl = baseExtUrl == null ? null : baseExtUrl.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public Integer getTokenValidTime() {
        return tokenValidTime;
    }

    public void setTokenValidTime(Integer tokenValidTime) {
        this.tokenValidTime = tokenValidTime;
    }

    public String getWlfItemNo() {
        return wlfItemNo;
    }

    public void setWlfItemNo(String wlfItemNo) {
        this.wlfItemNo = wlfItemNo == null ? null : wlfItemNo.trim();
    }

    public String getVmiWarehouse() {
        return vmiWarehouse;
    }

    public void setVmiWarehouse(String vmiWarehouse) {
        this.vmiWarehouse = vmiWarehouse == null ? null : vmiWarehouse.trim();
    }

    public String getSettleDefault() {
        return settleDefault;
    }

    public void setSettleDefault(String settleDefault) {
        this.settleDefault = settleDefault == null ? null : settleDefault.trim();
    }

    public String getSettleCashOnline() {
        return settleCashOnline;
    }

    public void setSettleCashOnline(String settleCashOnline) {
        this.settleCashOnline = settleCashOnline == null ? null : settleCashOnline.trim();
    }

    public String getSettleCashOffline() {
        return settleCashOffline;
    }

    public void setSettleCashOffline(String settleCashOffline) {
        this.settleCashOffline = settleCashOffline == null ? null : settleCashOffline.trim();
    }

    public String getSettleMonth() {
        return settleMonth;
    }

    public void setSettleMonth(String settleMonth) {
        this.settleMonth = settleMonth == null ? null : settleMonth.trim();
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