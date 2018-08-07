package com.bat.order.dao.local.dataobject;

import java.util.Date;

public class OrderDetailPictureLocalDO {
    private Integer id;

    private Integer orderId;

    private Integer orderGoodsDiyId;

    private String imageUrl;

    private String generateImageUrl;

    private String labelUrl;

    private Date createTime;

    private Date updateTime;

    private String rootPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderGoodsDiyId() {
        return orderGoodsDiyId;
    }

    public void setOrderGoodsDiyId(Integer orderGoodsDiyId) {
        this.orderGoodsDiyId = orderGoodsDiyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getGenerateImageUrl() {
        return generateImageUrl;
    }

    public void setGenerateImageUrl(String generateImageUrl) {
        this.generateImageUrl = generateImageUrl == null ? null : generateImageUrl.trim();
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl == null ? null : labelUrl.trim();
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

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath == null ? null : rootPath.trim();
    }
}