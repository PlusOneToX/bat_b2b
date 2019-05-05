package com.bat.dubboapi.goods.brand.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class BrandDistributorRpcCmd implements Serializable {

    private Integer distributorId;

    private Integer brandId;

    private Short operationType;

    private Integer scalePriceId;

    public Integer getScalePriceId() {
        return scalePriceId;
    }

    public void setScalePriceId(Integer scalePriceId) {
        this.scalePriceId = scalePriceId;
    }

    public Short getOperationType() {
        return operationType;
    }

    public void setOperationType(Short operationType) {
        this.operationType = operationType;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
}
