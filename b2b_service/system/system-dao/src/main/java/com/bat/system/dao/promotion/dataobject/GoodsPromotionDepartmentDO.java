package com.bat.system.dao.promotion.dataobject;

public class GoodsPromotionDepartmentDO {
    private Integer id;

    private Integer goodsPromotionId;

    private Integer departmentId;

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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}