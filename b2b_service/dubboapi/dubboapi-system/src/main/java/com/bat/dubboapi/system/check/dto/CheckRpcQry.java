package com.bat.dubboapi.system.check.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CheckRpcQry implements Serializable {
    private Integer id;

    private Short ext;

    private Integer subExt;

    private List<Integer> subExts;

    private Integer subExt1;

    private Integer applyUser;

    private Short status;

    private Integer lastCheckUser;

    private Integer nextCheckUser;

    private Short throughCheckCount;

    private Short checkUserCount;

    private Date createTime;

    private Date updateTime;

    private Date lastCheckTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getExt() {
        return ext;
    }

    public void setExt(Short ext) {
        this.ext = ext;
    }

    public Integer getSubExt() {
        return subExt;
    }

    public void setSubExt(Integer subExt) {
        this.subExt = subExt;
    }

    public Integer getSubExt1() {
        return subExt1;
    }

    public void setSubExt1(Integer subExt1) {
        this.subExt1 = subExt1;
    }

    public Integer getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(Integer applyUser) {
        this.applyUser = applyUser;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getLastCheckUser() {
        return lastCheckUser;
    }

    public void setLastCheckUser(Integer lastCheckUser) {
        this.lastCheckUser = lastCheckUser;
    }

    public Integer getNextCheckUser() {
        return nextCheckUser;
    }

    public void setNextCheckUser(Integer nextCheckUser) {
        this.nextCheckUser = nextCheckUser;
    }

    public Short getThroughCheckCount() {
        return throughCheckCount;
    }

    public void setThroughCheckCount(Short throughCheckCount) {
        this.throughCheckCount = throughCheckCount;
    }

    public Short getCheckUserCount() {
        return checkUserCount;
    }

    public void setCheckUserCount(Short checkUserCount) {
        this.checkUserCount = checkUserCount;
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

    public Date getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(Date lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public List<Integer> getSubExts() {
        return subExts;
    }

    public void setSubExts(List<Integer> subExts) {
        this.subExts = subExts;
    }

    @Override
    public String toString() {
        return "CheckRpcQry{" + "id=" + id + ", ext=" + ext + ", subExt=" + subExt + ", subExts=" + subExts
            + ", subExt1=" + subExt1 + ", applyUser=" + applyUser + ", status=" + status + ", lastCheckUser="
            + lastCheckUser + ", nextCheckUser=" + nextCheckUser + ", throughCheckCount=" + throughCheckCount
            + ", checkUserCount=" + checkUserCount + ", createTime=" + createTime + ", updateTime=" + updateTime
            + ", lastCheckTime=" + lastCheckTime + '}';
    }
}