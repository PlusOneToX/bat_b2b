package com.bat.financial.dao.distributoraccount.dataobject;

import java.util.Date;

public class AccountAlipayDistributorDO {
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String distributorCompanyName;

    private String accountName;

    private String appId;

    private Date createTime;

    private Date updateTime;

    private String appPublicSecret;

    private String appPrivateSecret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorCompanyName() {
        return distributorCompanyName;
    }

    public void setDistributorCompanyName(String distributorCompanyName) {
        this.distributorCompanyName = distributorCompanyName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
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

    public String getAppPublicSecret() {
        return appPublicSecret;
    }

    public void setAppPublicSecret(String appPublicSecret) {
        this.appPublicSecret = appPublicSecret == null ? null : appPublicSecret.trim();
    }

    public String getAppPrivateSecret() {
        return appPrivateSecret;
    }

    public void setAppPrivateSecret(String appPrivateSecret) {
        this.appPrivateSecret = appPrivateSecret == null ? null : appPrivateSecret.trim();
    }
}