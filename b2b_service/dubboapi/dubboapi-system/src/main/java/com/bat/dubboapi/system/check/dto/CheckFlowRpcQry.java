package com.bat.dubboapi.system.check.dto;

import java.io.Serializable;
import java.util.Date;

public class CheckFlowRpcQry implements Serializable {
    private Integer id;

    private Integer checkId;

    private Integer checkUser;

    private Short checkStatus;

    private Date checkTime;

    private String remark;

    private Integer checkOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public Integer getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(Integer checkUser) {
        this.checkUser = checkUser;
    }

    public Short getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Short checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCheckOrder() {
        return checkOrder;
    }

    public void setCheckOrder(Integer checkOrder) {
        this.checkOrder = checkOrder;
    }

    @Override
    public String toString() {
        return "CheckFlowRpcQry{" + "id=" + id + ", checkId=" + checkId + ", checkUser=" + checkUser + ", checkStatus="
            + checkStatus + ", checkTime=" + checkTime + ", remark='" + remark + '\'' + ", checkOrder=" + checkOrder
            + '}';
    }
}