package com.bat.order.service.common.constant;

/**
 * 运费常量类
 */
public class LogisticsConstant {

    /**
     * 订单结算方式 1、重量 2、体积
     */
    public static final Short LOGISTICS_BILLING_METHOD_WEIGHT = 1;

    public static final Short LOGISTICS_BILLING_METHOD_VOLUME = 2;

    /**
     * 适用范围 1、普通商品、2定制商品 3、普通商品和定制商品
     */
    public static final Short LOGISTICS_USE_RANGE_GENERAL = 1;

    public static final Short LOGISTICS_USE_RANGE_DIY = 2;

    public static final Short LOGISTICS_USE_RANGE_MERGE = 3;
}
