package com.bat.system.dao.promotion.dataobject;

public class GoodsPromotionDistributorGradeDO {
    private Integer id;

    private Integer goodsPromotionId;

    private Integer distributorGradeId;

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

    public Integer getDistributorGradeId() {
        return distributorGradeId;
    }

    public void setDistributorGradeId(Integer distributorGradeId) {
        this.distributorGradeId = distributorGradeId;
    }
}