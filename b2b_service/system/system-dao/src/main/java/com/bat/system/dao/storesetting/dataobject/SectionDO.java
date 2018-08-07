package com.bat.system.dao.storesetting.dataobject;

import java.util.Date;

public class SectionDO {
    private Integer id;

    private String title;

    private String titleEn;

    private Integer sort;

    private Short releaseStatus;

    private String imageUrl;

    private String extensionUrl;

    private String imageUrlEn;

    private String extensionUrlEn;

    private Short styleType;

    private Short styleTypeEn;

    private Short sectionArea;

    private String styleUrl;

    private String styleExtensionUrl;

    private String styleUrl1;

    private String styleExtensionUrl1;

    private String styleUrl2;

    private String styleExtensionUrl2;

    private String styleUrl3;

    private String styleExtensionUrl3;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getExtensionUrl() {
        return extensionUrl;
    }

    public void setExtensionUrl(String extensionUrl) {
        this.extensionUrl = extensionUrl == null ? null : extensionUrl.trim();
    }

    public String getImageUrlEn() {
        return imageUrlEn;
    }

    public void setImageUrlEn(String imageUrlEn) {
        this.imageUrlEn = imageUrlEn == null ? null : imageUrlEn.trim();
    }

    public String getExtensionUrlEn() {
        return extensionUrlEn;
    }

    public void setExtensionUrlEn(String extensionUrlEn) {
        this.extensionUrlEn = extensionUrlEn == null ? null : extensionUrlEn.trim();
    }

    public Short getStyleType() {
        return styleType;
    }

    public void setStyleType(Short styleType) {
        this.styleType = styleType;
    }

    public Short getStyleTypeEn() {
        return styleTypeEn;
    }

    public void setStyleTypeEn(Short styleTypeEn) {
        this.styleTypeEn = styleTypeEn;
    }

    public Short getSectionArea() {
        return sectionArea;
    }

    public void setSectionArea(Short sectionArea) {
        this.sectionArea = sectionArea;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl == null ? null : styleUrl.trim();
    }

    public String getStyleExtensionUrl() {
        return styleExtensionUrl;
    }

    public void setStyleExtensionUrl(String styleExtensionUrl) {
        this.styleExtensionUrl = styleExtensionUrl == null ? null : styleExtensionUrl.trim();
    }

    public String getStyleUrl1() {
        return styleUrl1;
    }

    public void setStyleUrl1(String styleUrl1) {
        this.styleUrl1 = styleUrl1 == null ? null : styleUrl1.trim();
    }

    public String getStyleExtensionUrl1() {
        return styleExtensionUrl1;
    }

    public void setStyleExtensionUrl1(String styleExtensionUrl1) {
        this.styleExtensionUrl1 = styleExtensionUrl1 == null ? null : styleExtensionUrl1.trim();
    }

    public String getStyleUrl2() {
        return styleUrl2;
    }

    public void setStyleUrl2(String styleUrl2) {
        this.styleUrl2 = styleUrl2 == null ? null : styleUrl2.trim();
    }

    public String getStyleExtensionUrl2() {
        return styleExtensionUrl2;
    }

    public void setStyleExtensionUrl2(String styleExtensionUrl2) {
        this.styleExtensionUrl2 = styleExtensionUrl2 == null ? null : styleExtensionUrl2.trim();
    }

    public String getStyleUrl3() {
        return styleUrl3;
    }

    public void setStyleUrl3(String styleUrl3) {
        this.styleUrl3 = styleUrl3 == null ? null : styleUrl3.trim();
    }

    public String getStyleExtensionUrl3() {
        return styleExtensionUrl3;
    }

    public void setStyleExtensionUrl3(String styleExtensionUrl3) {
        this.styleExtensionUrl3 = styleExtensionUrl3 == null ? null : styleExtensionUrl3.trim();
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