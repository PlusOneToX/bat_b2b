package com.bat.promotion.dao.coupon.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CouponCustomerDO {
    private Integer id;

    private Integer couponId;

    private String couponNo;

    private Integer pieces;

    private String invalidExplain;

    private Short couponStatus;

    private Integer customerId;

    private String customerName;

    private String openId;

    private String platform;

    private Integer distributorId;

    private Date createTime;

    private Date updateTime;

}