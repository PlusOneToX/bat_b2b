package com.bat.dubboapi.goods.common;


import java.io.Serializable;

public class BrandCategoryScalePrice implements Serializable {
    private Integer brandId;
    private Integer categoryId;
    private Integer scalePriceId;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getScalePriceId() {
        return scalePriceId;
    }

    public void setScalePriceId(Integer scalePriceId) {
        this.scalePriceId = scalePriceId;
    }
}