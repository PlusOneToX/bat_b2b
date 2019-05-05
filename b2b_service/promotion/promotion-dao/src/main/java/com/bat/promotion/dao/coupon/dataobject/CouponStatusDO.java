package com.bat.promotion.dao.coupon.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CouponStatusDO {
    private Integer id;
    private String couponNo;
    private Short couponStatus;
    private String invalidExplain;
    private Date updateTime;
}