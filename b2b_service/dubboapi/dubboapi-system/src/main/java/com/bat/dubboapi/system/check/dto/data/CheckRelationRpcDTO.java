package com.bat.dubboapi.system.check.dto.data;

import java.io.Serializable;
import java.util.Date;

public class CheckRelationRpcDTO implements Serializable {
    private Integer id;

    private Short ext;

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