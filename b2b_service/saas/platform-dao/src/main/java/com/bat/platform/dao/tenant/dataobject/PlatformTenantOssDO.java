package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

public class PlatformTenantOssDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private Short ossType;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucket;

    private String baseHttp;

    private String roleArn;

    private String regionId;

    private String region;

    private String policy;

    private String distributorOssFolder;

    private Date createTime;

    private Date updateTime;

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

    public Short getOssType() {
        return ossType;
    }

    public void setOssType(Short ossType) {
        this.ossType = ossType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint == null ? null : endpoint.trim();
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId == null ? null : accessKeyId.trim();
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret == null ? null : accessKeySecret.trim();
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket == null ? null : bucket.trim();
    }

    public String getBaseHttp() {
        return baseHttp;
    }

    public void setBaseHttp(String baseHttp) {
        this.baseHttp = baseHttp == null ? null : baseHttp.trim();
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn == null ? null : roleArn.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy == null ? null : policy.trim();
    }

    public String getDistributorOssFolder() {
        return distributorOssFolder;
    }

    public void setDistributorOssFolder(String distributorOssFolder) {
        this.distributorOssFolder = distributorOssFolder == null ? null : distributorOssFolder.trim();
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