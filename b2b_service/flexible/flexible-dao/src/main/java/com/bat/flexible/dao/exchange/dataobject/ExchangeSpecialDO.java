package com.bat.flexible.dao.exchange.dataobject;

import java.util.Date;

public class ExchangeSpecialDO {
    private Integer id;

    private Short activityPlatform;

    private String title;

    private String pageImg;

    private String pageGuideText;

    private Short type;

    private Integer exchangeId;

    private String forwardText;

    private String forwardImg;

    private String friendImg;

    private Date endTime;

    private Short status;

    private Date createTime;

    private Date updateTime;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getPageImg() {
        return pageImg;
    }

    public void setPageImg(String pageImg) {
        this.pageImg = pageImg == null ? null : pageImg.trim();
    }

    public String getPageGuideText() {
        return pageGuideText;
    }

    public void setPageGuideText(String pageGuideText) {
        this.pageGuideText = pageGuideText == null ? null : pageGuideText.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getForwardText() {
        return forwardText;
    }

    public void setForwardText(String forwardText) {
        this.forwardText = forwardText == null ? null : forwardText.trim();
    }

    public String getForwardImg() {
        return forwardImg;
    }

    public void setForwardImg(String forwardImg) {
        this.forwardImg = forwardImg == null ? null : forwardImg.trim();
    }

    public String getFriendImg() {
        return friendImg;
    }

    public void setFriendImg(String friendImg) {
        this.friendImg = friendImg == null ? null : friendImg.trim();
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