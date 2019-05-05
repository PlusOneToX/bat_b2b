package com.bat.promotion.dao.coupon.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CouponDO {
    private Integer id;

    private String name;

    private String couponDesc;

    private String invalidExplain;

    private Integer generateCount;

    private Integer generatedCount;

    private Integer usedCount;

    private Integer limitCount;

    private Short applyStatus;

    private Short couponStatus;

    private Date startTime;

    private Date endTime;

    private Short receivedType;

    private Short couponMethod;

    private BigDecimal orderMoney;

    private BigDecimal reduction;

    private BigDecimal discount;

    private Short couponType;

    private String couponCode;

    private Short deliveryFeeFlag;

    private BigDecimal deliveryFee;

    private Short modelScope;

    private Short materialScope;

    private Short couponScope;

    private Date createTime;

    private Date updateTime;

    private String couponExplain;
}