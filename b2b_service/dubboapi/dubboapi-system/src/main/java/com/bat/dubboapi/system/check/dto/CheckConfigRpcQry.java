package com.bat.dubboapi.system.check.dto;

import java.io.Serializable;
import java.util.Date;

public class CheckConfigRpcQry implements Serializable {
    private Integer id;

    private Short ext;

    private Integer checkUser;

    private Short checkOrder;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

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

    public Integer getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(Integer checkUser) {
        this.checkUser = checkUser;
    }

    public Short getCheckOrder() {
        return checkOrder;
    }

    public void setCheckOrder(Short checkOrder) {
        this.checkOrder = checkOrder;
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
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

    @Override
    public String toString() {
        return "CheckConfigRpcQry{" + "id=" + id + ", ext=" + ext + ", checkUser=" + checkUser + ", checkOrder="
            + checkOrder + ", openFlag=" + openFlag + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}