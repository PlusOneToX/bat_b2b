package com.bat.system.dao.check.dataobject;

import java.util.Date;

public class CheckConfigDO {
    private Integer id;

    private Short ext;

    private Integer checkUser;

    private Integer checkOrder;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

    /**
     * 扩展数据 审核人名称
     */
    private String checkUserName;

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

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

    public Integer getCheckOrder() {
        return checkOrder;
    }

    public void setCheckOrder(Integer checkOrder) {
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
}