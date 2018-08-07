package com.bat.system.dao.promotion.dataobject;

import java.util.Date;

public class GoodsPromotionDO {
    private Integer id;

    private String extensionGoodsName;

    private String pcEnExtensionImgUrl;

    private String pcEnExtensionGoodsUrl;

    private String pcCnExtensionImgUrl;

    private String pcCnExtensionGoodsUrl;

    private Short distributorScope;

    private Date extensionStartTime;

    private Date extensionEndTime;

    private Date createTime;

    private Date updateTime;

    private String moEnExtensionImgUrl;

    private String moEnExtensionGoodsUrl;

    private String moCnExtensionImgUrl;

    private String moCnExtensionGoodsUrl;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtensionGoodsName() {
        return extensionGoodsName;
    }

    public void setExtensionGoodsName(String extensionGoodsName) {
        this.extensionGoodsName = extensionGoodsName == null ? null : extensionGoodsName.trim();
    }

    public String getPcEnExtensionImgUrl() {
        return pcEnExtensionImgUrl;
    }

    public void setPcEnExtensionImgUrl(String pcEnExtensionImgUrl) {
        this.pcEnExtensionImgUrl = pcEnExtensionImgUrl == null ? null : pcEnExtensionImgUrl.trim();
    }

    public String getPcEnExtensionGoodsUrl() {
        return pcEnExtensionGoodsUrl;
    }

    public void setPcEnExtensionGoodsUrl(String pcEnExtensionGoodsUrl) {
        this.pcEnExtensionGoodsUrl = pcEnExtensionGoodsUrl == null ? null : pcEnExtensionGoodsUrl.trim();
    }

    public String getPcCnExtensionImgUrl() {
        return pcCnExtensionImgUrl;
    }

    public void setPcCnExtensionImgUrl(String pcCnExtensionImgUrl) {
        this.pcCnExtensionImgUrl = pcCnExtensionImgUrl == null ? null : pcCnExtensionImgUrl.trim();
    }

    public String getPcCnExtensionGoodsUrl() {
        return pcCnExtensionGoodsUrl;
    }

    public void setPcCnExtensionGoodsUrl(String pcCnExtensionGoodsUrl) {
        this.pcCnExtensionGoodsUrl = pcCnExtensionGoodsUrl == null ? null : pcCnExtensionGoodsUrl.trim();
    }

    public Short getDistributorScope() {
        return distributorScope;
    }

    public void setDistributorScope(Short distributorScope) {
        this.distributorScope = distributorScope;
    }

    public Date getExtensionStartTime() {
        return extensionStartTime;
    }

    public void setExtensionStartTime(Date extensionStartTime) {
        this.extensionStartTime = extensionStartTime;
    }

    public Date getExtensionEndTime() {
        return extensionEndTime;
    }

    public void setExtensionEndTime(Date extensionEndTime) {
        this.extensionEndTime = extensionEndTime;
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

    public String getMoEnExtensionImgUrl() {
        return moEnExtensionImgUrl;
    }

    public void setMoEnExtensionImgUrl(String moEnExtensionImgUrl) {
        this.moEnExtensionImgUrl = moEnExtensionImgUrl == null ? null : moEnExtensionImgUrl.trim();
    }

    public String getMoEnExtensionGoodsUrl() {
        return moEnExtensionGoodsUrl;
    }

    public void setMoEnExtensionGoodsUrl(String moEnExtensionGoodsUrl) {
        this.moEnExtensionGoodsUrl = moEnExtensionGoodsUrl == null ? null : moEnExtensionGoodsUrl.trim();
    }

    public String getMoCnExtensionImgUrl() {
        return moCnExtensionImgUrl;
    }

    public void setMoCnExtensionImgUrl(String moCnExtensionImgUrl) {
        this.moCnExtensionImgUrl = moCnExtensionImgUrl == null ? null : moCnExtensionImgUrl.trim();
    }

    public String getMoCnExtensionGoodsUrl() {
        return moCnExtensionGoodsUrl;
    }

    public void setMoCnExtensionGoodsUrl(String moCnExtensionGoodsUrl) {
        this.moCnExtensionGoodsUrl = moCnExtensionGoodsUrl == null ? null : moCnExtensionGoodsUrl.trim();
    }
}