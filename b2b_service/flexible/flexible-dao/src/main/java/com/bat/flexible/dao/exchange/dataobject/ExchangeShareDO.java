package com.bat.flexible.dao.exchange.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class ExchangeShareDO {
    private Integer id;

    private Short activityPlatform;

    private String preferName;

    private Short seat;

    private String forwardButtonText;

    private Short reduceFlag;

    private BigDecimal reduceAmount;

    private Integer exchangeSpecialId;

    private Short distributorVisualType;

    private Date createTime;

    private Date updateTime;

    private Date startTime;

    private Date endTime;

    private Short status;

    private Integer forwardNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getActivityPlatform() {
        return activityPlatform;
    }

    public void setActivityPlatform(Short activityPlatform) {
        this.activityPlatform = activityPlatform;
    }

    public String getPreferName() {
        return preferName;
    }

    public void setPreferName(String preferName) {
        this.preferName = preferName == null ? null : preferName.trim();
    }

    public Short getSeat() {
        return seat;
    }

    public void setSeat(Short seat) {
        this.seat = seat;
    }

    public String getForwardButtonText() {
        return forwardButtonText;
    }

    public void setForwardButtonText(String forwardButtonText) {
        this.forwardButtonText = forwardButtonText == null ? null : forwardButtonText.trim();
    }

    public Short getReduceFlag() {
        return reduceFlag;
    }

    public void setReduceFlag(Short reduceFlag) {
        this.reduceFlag = reduceFlag;
    }

    public BigDecimal getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(BigDecimal reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Integer getExchangeSpecialId() {
        return exchangeSpecialId;
    }

    public void setExchangeSpecialId(Integer exchangeSpecialId) {
        this.exchangeSpecialId = exchangeSpecialId;
    }

    public Short getDistributorVisualType() {
        return distributorVisualType;
    }

    public void setDistributorVisualType(Short distributorVisualType) {
        this.distributorVisualType = distributorVisualType;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getForwardNum() {
        return forwardNum;
    }

    public void setForwardNum(Integer forwardNum) {
        this.forwardNum = forwardNum;
    }
}