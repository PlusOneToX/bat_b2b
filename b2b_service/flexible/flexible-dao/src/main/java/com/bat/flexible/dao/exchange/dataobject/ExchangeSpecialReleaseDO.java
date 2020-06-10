package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeSpecialReleaseDO {
    private Integer id;

    private Integer exchangeShareId;

    private Integer exchangeSpecialId;

    private Integer exchangeSpecialDistributorId;

    private Integer distributorId;

    private Integer userId;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeShareId() {
        return exchangeShareId;
    }

    public void setExchangeShareId(Integer exchangeShareId) {
        this.exchangeShareId = exchangeShareId;
    }

    public Integer getExchangeSpecialId() {
        return exchangeSpecialId;
    }

    public void setExchangeSpecialId(Integer exchangeSpecialId) {
        this.exchangeSpecialId = exchangeSpecialId;
    }

    public Integer getExchangeSpecialDistributorId() {
        return exchangeSpecialDistributorId;
    }

    public void setExchangeSpecialDistributorId(Integer exchangeSpecialDistributorId) {
        this.exchangeSpecialDistributorId = exchangeSpecialDistributorId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}