package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeCodeSyncBackLogDO {
    private Integer id;

    private Integer itemId;

    private Integer exchangeId;

    private Integer exchangeCodeId;

    private Date createTime;

    private Integer alreadySyncBoxCount;

    private Integer alreadySyncPlainCodeCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
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

    public Integer getAlreadySyncBoxCount() {
        return alreadySyncBoxCount;
    }

    public void setAlreadySyncBoxCount(Integer alreadySyncBoxCount) {
        this.alreadySyncBoxCount = alreadySyncBoxCount;
    }

    public Integer getAlreadySyncPlainCodeCount() {
        return alreadySyncPlainCodeCount;
    }

    public void setAlreadySyncPlainCodeCount(Integer alreadySyncPlainCodeCount) {
        this.alreadySyncPlainCodeCount = alreadySyncPlainCodeCount;
    }
}