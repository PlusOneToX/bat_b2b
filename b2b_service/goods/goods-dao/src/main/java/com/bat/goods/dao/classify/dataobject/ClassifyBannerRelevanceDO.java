package com.bat.goods.dao.classify.dataobject;

public class ClassifyBannerRelevanceDO {
    private Integer id;

    private Integer classifyId;

    private String imgUrl;

    private String jumpUrl;

    private Short sort;

    private Short recommendFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl == null ? null : jumpUrl.trim();
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

    public Short getRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(Short recommendFlag) {
        this.recommendFlag = recommendFlag;
    }
}