package com.bat.promotion.dao.coupon.dataobject;

import lombok.Data;

@Data
public class CouponMaterialRelevanceDO {
    private Integer id;

    private Integer couponId;

    private Integer materialId;

    private String materialName;
}