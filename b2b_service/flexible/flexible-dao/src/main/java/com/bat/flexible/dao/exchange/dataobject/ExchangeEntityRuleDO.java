package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeEntityRuleDO {
    private Integer id;

    private Integer exchangeId;

    private Short cardType;

    private Short ruleType;

    private String riseValue;

    private String floatValue;

    private Integer randomValue;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

    private Short isSyncFactory;

    private Integer boxNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Short getCardType() {
        return cardType;
    }

    public void setCardType(Short cardType) {
        this.cardType = cardType;
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public String getRiseValue() {
        return riseValue;
    }

    public void setRiseValue(String riseValue) {
        this.riseValue = riseValue == null ? null : riseValue.trim();
    }

    public String getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(String floatValue) {
        this.floatValue = floatValue == null ? null : floatValue.trim();
    }

    public Integer getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(Integer randomValue) {
        this.randomValue = randomValue;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsSyncFactory() {
        return isSyncFactory;
    }

    public void setIsSyncFactory(Short isSyncFactory) {
        this.isSyncFactory = isSyncFactory;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }
}