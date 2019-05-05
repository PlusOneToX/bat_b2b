package com.bat.dubboapi.distributor.distributor.dto.data;


import java.io.Serializable;

public class OneScalePriceRpcDTO implements Serializable {
    private Integer brandId;

    private Integer categoryId;

    private Integer scalePriceId;

    private Integer distributionScalePriceId;

    public Integer getDistributionScalePriceId() {
        return distributionScalePriceId;
    }

    public void setDistributionScalePriceId(Integer distributionScalePriceId) {
        this.distributionScalePriceId = distributionScalePriceId;
    }

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