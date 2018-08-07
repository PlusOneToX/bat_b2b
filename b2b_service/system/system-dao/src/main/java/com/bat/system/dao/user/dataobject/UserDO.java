package com.bat.system.dao.user.dataobject;

import java.util.Date;

public class UserDO {
    private Integer id;

    private String userName;

    private String realName;

    private String password;

    private String mobile;

    private String email;

    private String dingAvatar;

    private Short adminType;

    private Short brandScope;

    private Short saleScope;

    private Short status;

    private String erpUserNo;

    private Integer organizationId;

    private Integer departmentId;

    private Long rockAccountId;

    private Short fictitiousFlag;

    private Short saleFlag;

    private String remark;

    private Date updateTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getDingAvatar() {
        return dingAvatar;
    }

    public void setDingAvatar(String dingAvatar) {
        this.dingAvatar = dingAvatar == null ? null : dingAvatar.trim();
    }

    public Short getAdminType() {
        return adminType;
    }

    public void setAdminType(Short adminType) {
        this.adminType = adminType;
    }

    public Short getBrandScope() {
        return brandScope;
    }

    public void setBrandScope(Short brandScope) {
        this.brandScope = brandScope;
    }

    public Short getSaleScope() {
        return saleScope;
    }

    public void setSaleScope(Short saleScope) {
        this.saleScope = saleScope;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getErpUserNo() {
        return erpUserNo;
    }

    public void setErpUserNo(String erpUserNo) {
        this.erpUserNo = erpUserNo == null ? null : erpUserNo.trim();
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Long getRockAccountId() {
        return rockAccountId;
    }

    public void setRockAccountId(Long rockAccountId) {
        this.rockAccountId = rockAccountId;
    }

    public Short getFictitiousFlag() {
        return fictitiousFlag;
    }

    public void setFictitiousFlag(Short fictitiousFlag) {
        this.fictitiousFlag = fictitiousFlag;
    }

    public Short getSaleFlag() {
        return saleFlag;
    }

    public void setSaleFlag(Short saleFlag) {
        this.saleFlag = saleFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}