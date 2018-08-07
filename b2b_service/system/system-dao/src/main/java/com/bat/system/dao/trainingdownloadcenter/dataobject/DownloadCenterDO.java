package com.bat.system.dao.trainingdownloadcenter.dataobject;

import java.util.Date;
import java.util.List;

public class DownloadCenterDO {
    private Integer id;

    private Integer parentId;

    private Integer sort;

    private Short status;

    private String titleZh;

    private String titleEn;

    private String contentUrlZh;

    private String contentUrlEn;

    private String thumbnailUrlZh;

    private String thumbnailUrlEn;

    private Date createTime;

    private Date updateTime;

    private List<DownloadCenterDO> childrens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getTitleZh() {
        return titleZh;
    }

    public void setTitleZh(String titleZh) {
        this.titleZh = titleZh == null ? null : titleZh.trim();
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn == null ? null : titleEn.trim();
    }

    public String getContentUrlZh() {
        return contentUrlZh;
    }

    public void setContentUrlZh(String contentUrlZh) {
        this.contentUrlZh = contentUrlZh == null ? null : contentUrlZh.trim();
    }

    public String getContentUrlEn() {
        return contentUrlEn;
    }

    public void setContentUrlEn(String contentUrlEn) {
        this.contentUrlEn = contentUrlEn == null ? null : contentUrlEn.trim();
    }

    public String getThumbnailUrlZh() {
        return thumbnailUrlZh;
    }

    public void setThumbnailUrlZh(String thumbnailUrlZh) {
        this.thumbnailUrlZh = thumbnailUrlZh == null ? null : thumbnailUrlZh.trim();
    }

    public String getThumbnailUrlEn() {
        return thumbnailUrlEn;
    }

    public void setThumbnailUrlEn(String thumbnailUrlEn) {
        this.thumbnailUrlEn = thumbnailUrlEn == null ? null : thumbnailUrlEn.trim();
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

    public List<DownloadCenterDO> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<DownloadCenterDO> childrens) {
        this.childrens = childrens;
    }
}