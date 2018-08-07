package com.bat.system.dao.globalsetting.dataobject;

import java.util.Date;

public class FactorySettingDelayPushDO {
    private Integer id;

    private String factory;

    private Date pushTime;

    private String useRange;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory == null ? null : factory.trim();
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange == null ? null : useRange.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}