package com.bat.goods.dao.goods.dataobject;

public class GoodsDistributorGroupRelevanceDO {
    private Integer id;

    private Integer goodsId;

    private Integer distributorGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getDistributorGroupId() {
        return distributorGroupId;
    }

    public void setDistributorGroupId(Integer distributorGroupId) {
        this.distributorGroupId = distributorGroupId;
    }
}