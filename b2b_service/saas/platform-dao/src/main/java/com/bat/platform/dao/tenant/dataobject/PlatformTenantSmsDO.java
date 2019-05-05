package com.bat.platform.dao.tenant.dataobject;

import java.util.Date;

public class PlatformTenantSmsDO {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private Short smsType;

    private String sign;

    private String accessKeyId;

    private String accessKeySecret;

    private Integer verifyCodeLength;

    private Integer codeVerifyTime;

    private Integer verifyCodeCountDown;

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

    public Short getSmsType() {
        return smsType;
    }

    public void setSmsType(Short smsType) {
        this.smsType = smsType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
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

    public Integer getVerifyCodeLength() {
        return verifyCodeLength;
    }

    public void setVerifyCodeLength(Integer verifyCodeLength) {
        this.verifyCodeLength = verifyCodeLength;
    }

    public Integer getCodeVerifyTime() {
        return codeVerifyTime;
    }

    public void setCodeVerifyTime(Integer codeVerifyTime) {
        this.codeVerifyTime = codeVerifyTime;
    }

    public Integer getVerifyCodeCountDown() {
        return verifyCodeCountDown;
    }

    public void setVerifyCodeCountDown(Integer verifyCodeCountDown) {
        this.verifyCodeCountDown = verifyCodeCountDown;
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