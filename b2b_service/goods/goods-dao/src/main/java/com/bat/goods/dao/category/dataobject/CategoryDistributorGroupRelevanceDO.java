package com.bat.goods.dao.category.dataobject;

public class CategoryDistributorGroupRelevanceDO {
    private Integer id;

    private Integer categoryId;

    private Integer distributorGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDistributorGroupId() {
        return distributorGroupId;
    }

    public void setDistributorGroupId(Integer distributorGroupId) {
        this.distributorGroupId = distributorGroupId;
    }
}