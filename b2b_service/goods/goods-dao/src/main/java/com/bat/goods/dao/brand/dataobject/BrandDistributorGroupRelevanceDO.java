package com.bat.goods.dao.brand.dataobject;

public class BrandDistributorGroupRelevanceDO {
    private Integer id;

    private Integer brandId;

    private Integer distributorGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getDistributorGroupId() {
        return distributorGroupId;
    }

    public void setDistributorGroupId(Integer distributorGroupId) {
        this.distributorGroupId = distributorGroupId;
    }
}