package com.bat.promotion.dao.coupon.dataobject;

import lombok.Data;

@Data
public class CouponModelRelevanceDO {
    private Integer id;

    private Integer couponId;

    private Integer modelId;

    private String modelName;
}