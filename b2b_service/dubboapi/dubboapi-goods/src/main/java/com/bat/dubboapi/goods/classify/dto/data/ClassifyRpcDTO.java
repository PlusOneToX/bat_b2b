package com.bat.dubboapi.goods.classify.dto.data;


import java.io.Serializable;
import java.util.Date;
public class ClassifyRpcDTO implements Serializable {

    private Integer id;
    private String name;
    private String nameEn;
    private String description;
    private Short openFlag;
    private Integer sort;
    private Integer parentId;
    private String imageUrl;
    private String imageUrlEn;
    private Date createTime;
    private Date updateTime;
    private Short recommendFlag;
    private String appletName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlEn() {
        return imageUrlEn;
    }

    public void setImageUrlEn(String imageUrlEn) {
        this.imageUrlEn = imageUrlEn;
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

    public Short getRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(Short recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public String getAppletName() {
        return appletName;
    }

    public void setAppletName(String appletName) {
        this.appletName = appletName;
    }
}
