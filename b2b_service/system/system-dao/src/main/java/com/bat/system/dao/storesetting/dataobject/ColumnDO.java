package com.bat.system.dao.storesetting.dataobject;

import java.util.Date;

public class ColumnDO {
    private Integer id;

    private String title;

    private String titleEn;

    private String bannerImg;

    private Integer sort;

    private Short releaseStatus;

    private Short columnArea;

    private Short distributorScope;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn == null ? null : titleEn.trim();
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Short releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Short getColumnArea() {
        return columnArea;
    }

    public void setColumnArea(Short columnArea) {
        this.columnArea = columnArea;
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
}