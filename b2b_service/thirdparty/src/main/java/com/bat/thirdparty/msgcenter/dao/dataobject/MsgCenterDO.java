package com.bat.thirdparty.msgcenter.dao.dataobject;

import java.util.Date;

public class MsgCenterDO {
    private Integer id;

    private Short channel;

    private String title;

    private String miniLink;

    private String pcLink;

    private Short pushSwitch;

    private Short distributorScope;

    private Date createTime;

    private Date updateTime;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getMiniLink() {
        return miniLink;
    }

    public void setMiniLink(String miniLink) {
        this.miniLink = miniLink == null ? null : miniLink.trim();
    }

    public String getPcLink() {
        return pcLink;
    }

    public void setPcLink(String pcLink) {
        this.pcLink = pcLink == null ? null : pcLink.trim();
    }

    public Short getPushSwitch() {
        return pushSwitch;
    }

    public void setPushSwitch(Short pushSwitch) {
        this.pushSwitch = pushSwitch;
    }

    public Short getDistributorScope() {
        return distributorScope;
    }

    public void setDistributorScope(Short distributorScope) {
        this.distributorScope = distributorScope;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}