package com.bat.flexible.dao.burying.dataobject;

import java.util.Date;

public class BuryingPointDO {
    private Integer id;

    private String source;

    private Integer userId;

    private Integer distributorId;

    private Short networkType;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Short networkType) {
        this.networkType = networkType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}