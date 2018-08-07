package com.bat.order.service.common.constant;

public class DistributorConstant {

    /**
     * 是否开启C端模式 0 不开启, 1 开启
     */
    public static final Short Extend_CUSTOMER_FLAG_YES = 1;

    public static final Short Extend_CUSTOMER_FLAG_NO = 0;

    /**
     * C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    public static final Short Extend_CUSTOMER_MODE_PLATFORM = 1;

    public static final Short Extend_CUSTOMER_MODE_PARENT = 2;

    public static final Short Extend_CUSTOMER_MODE_SELF = 3;

    /**
     * 是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值）
     */
    public static final Short Extend_SUB_ACCOUNT_FLAG_YES = 1;

    public static final Short Extend_SUB_ACCOUNT_FLAG_NO = 0;

    // 分账金额类型 1、按照实付金额 2、按照利润金额
    public static final Short SUB_ACCOUNT_TYPE_ACTUAL_PAYMENT = 1;

    public static final Short SUB_ACCOUNT_TYPE_PROFIT = 2;

}
