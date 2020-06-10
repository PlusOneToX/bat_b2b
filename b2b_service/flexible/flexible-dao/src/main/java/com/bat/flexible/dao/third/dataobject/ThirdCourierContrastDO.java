package com.bat.flexible.dao.third.dataobject;

import java.util.Date;

public class ThirdCourierContrastDO {
    private Integer id;

    private Integer distributorId;

    private String distributionKdnCode;

    private String distributorShipperName;

    private String distributorShipperCode;

    private Short openFlag;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

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

    public String getDistributionKdnCode() {
        return distributionKdnCode;
    }

    public void setDistributionKdnCode(String distributionKdnCode) {
        this.distributionKdnCode = distributionKdnCode == null ? null : distributionKdnCode.trim();
    }

    public String getDistributorShipperName() {
        return distributorShipperName;
    }

    public void setDistributorShipperName(String distributorShipperName) {
        this.distributorShipperName = distributorShipperName == null ? null : distributorShipperName.trim();
    }

    public String getDistributorShipperCode() {
        return distributorShipperCode;
    }

    public void setDistributorShipperCode(String distributorShipperCode) {
        this.distributorShipperCode = distributorShipperCode == null ? null : distributorShipperCode.trim();
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}