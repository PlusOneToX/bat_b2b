package com.bat.system.dao.promotion.dataobject;

public class GoodsPromotionDistributorDO {
    private Integer id;

    private Integer goodsPromotionId;

    private Integer distributorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsPromotionId() {
        return goodsPromotionId;
    }

    public void setGoodsPromotionId(Integer goodsPromotionId) {
        this.goodsPromotionId = goodsPromotionId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }
}