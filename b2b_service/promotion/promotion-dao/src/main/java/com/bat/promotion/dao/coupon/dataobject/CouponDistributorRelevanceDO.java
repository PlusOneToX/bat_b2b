package com.bat.promotion.dao.coupon.dataobject;

import lombok.Data;

@Data
public class CouponDistributorRelevanceDO {
    private Integer id;

    private Integer couponId;

    private Integer distributorId;

    private String name;
    private String companyName;

}