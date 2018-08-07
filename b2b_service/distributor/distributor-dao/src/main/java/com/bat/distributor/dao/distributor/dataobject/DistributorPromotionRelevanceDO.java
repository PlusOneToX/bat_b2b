package com.bat.distributor.dao.distributor.dataobject;

public class DistributorPromotionRelevanceDO {
    private Integer id;

    private Integer distributorId;

    private String promotionIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(String promotionIds) {
        this.promotionIds = promotionIds == null ? null : promotionIds.trim();
    }
}