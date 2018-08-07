package com.bat.system.dao.storesetting.dataobject;

import java.util.Date;

public class BannerDO {
    private Integer id;

    private String imageUrl;

    private String bannerUrl;

    private Short bannerArea;

    private Integer sort;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
    }

    public Short getBannerArea() {
        return bannerArea;
    }

    public void setBannerArea(Short bannerArea) {
        this.bannerArea = bannerArea;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}