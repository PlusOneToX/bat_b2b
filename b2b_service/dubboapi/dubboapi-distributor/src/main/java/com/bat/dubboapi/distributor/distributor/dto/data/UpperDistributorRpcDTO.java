package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.Date;

public class UpperDistributorRpcDTO implements Serializable {
    private Integer upperDistributorId;

    private String upperDistributorName;

    private String openId;

    private Integer distributorId;

    private String distributorName;

    private Date checkTime;

    private Date applyTime;

    public Integer getUpperDistributorId() {
        return upperDistributorId;
    }

    public void setUpperDistributorId(Integer upperDistributorId) {
        this.upperDistributorId = upperDistributorId;
    }

    public String getUpperDistributorName() {
        return upperDistributorName;
    }

    public void setUpperDistributorName(String upperDistributorName) {
        this.upperDistributorName = upperDistributorName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
}