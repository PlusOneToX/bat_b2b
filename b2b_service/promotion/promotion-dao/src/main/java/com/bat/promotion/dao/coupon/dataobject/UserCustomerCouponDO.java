package com.bat.promotion.dao.coupon.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class UserCustomerCouponDO {
    private Integer id;
    private Integer couponId;
    private String couponNo;
    private Integer pieces;
    private String couponName;
    private String couponDesc;
    private String couponExplain;
    private String invalidExplain;
    private Short couponStatus;
    private Date startTime;
    private Date endTime;
    private Short couponMethod;
    private BigDecimal orderMoney;
    private BigDecimal reduction;
    private BigDecimal discount;
    private Short couponType;
    private Short deliveryFeeFlag;
    private BigDecimal deliveryFee;
    private Short modelScope;
    private Short materialScope;
    private Short receivedFlag;
    private Short goodsEnable;
    private Short amountEnable;
    private Date createTime;
    private Integer customerId;
    private Integer distributorId;
}