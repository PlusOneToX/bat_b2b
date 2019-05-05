package com.bat.thirdparty.order.api.dto.zhaolianji;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @date 2019/06/9
 * @time 17:09
 * 这个实体类特殊、请勿加参数
 */
public class ZhaoLiangJiCustomInfo extends Object implements Serializable {
    private String image;
    private Integer materialId;
    private Integer modelId;
    private String modelName;
    private Integer brandId;
    private String brandName;
    private String generateImage;
    private Integer pictureId;
    private BigDecimal price;
    private String skuNo;
    private Integer count;
    private BigDecimal totalPrice;
    private String skuId = null;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getGenerateImage() {
        return generateImage;
    }

    public void setGenerateImage(String generateImage) {
        this.generateImage = generateImage;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("image", image)
                .append("materialId", materialId)
                .append("modelId", modelId)
                .append("modelName", modelName)
                .append("brandId", brandId)
                .append("brandName", brandName)
                .append("generateImage", generateImage)
                .append("pictureId", pictureId)
                .append("price", price)
                .append("skuId", skuId)
                .toString();
    }
}
