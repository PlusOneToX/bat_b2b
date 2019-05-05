package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;

public class OrderGoodsDiyCmd implements Serializable {
    /**
     * 产品类型id
     */
    private Integer categoryId;
    /**
     * 产品类型名称
     */
    private String categoryName;
    /**
     * 材质id
     */
    private Integer materialId;
    /**
     * 材质名称
     */
    private String materialName;
    /**
     * 生产商 YC云创 MK麦客 LHW联辉王
     */
    private String manufactors;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 型号id
     */
    private Integer modelId;
    /**
     * 型号名称
     */
    private String modelName;
    /**
     * 图片id（网络图传0）
     */
    private Integer pictureId;
    /**
     * 图片名称
     */
    private String pictureName;
    /**
     * 生产图URL地址
     */
    private String generateImage;
    /**
     * 预览图URL地址
     */
    private String previewImage;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getManufactors() {
        return manufactors;
    }

    public void setManufactors(String manufactors) {
        this.manufactors = manufactors;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getGenerateImage() {
        return generateImage;
    }

    public void setGenerateImage(String generateImage) {
        this.generateImage = generateImage;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }
}
