package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;

public class BrandScaleRpc implements Serializable {

    private Integer brandId;
    private Integer scalePriceId;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getScalePriceId() {
        return scalePriceId;
    }

    public void setScalePriceId(Integer scalePriceId) {
        this.scalePriceId = scalePriceId;
    }
}
