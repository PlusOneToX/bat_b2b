package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeSpecialReceiveDO {
    private Integer id;

    private Integer specialReleaseId;

    private Integer userId;

    private Integer exchangeCodeId;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSpecialReleaseId() {
        return specialReleaseId;
    }

    public void setSpecialReleaseId(Integer specialReleaseId) {
        this.specialReleaseId = specialReleaseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExchangeCodeId() {
        return exchangeCodeId;
    }

    public void setExchangeCodeId(Integer exchangeCodeId) {
        this.exchangeCodeId = exchangeCodeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}