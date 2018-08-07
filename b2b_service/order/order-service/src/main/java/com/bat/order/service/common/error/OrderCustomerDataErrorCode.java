package com.bat.order.service.common.error;

public class OrderCustomerDataErrorCode {

    /**
     * 店铺编码错误
     */
    public static final String ORDER_DISTRIBUTOR_SHOP_CODE_ERROR = "ORDER_DISTRIBUTOR_SHOP_CODE_ERROR";

    /**
     * 店铺已被停用
     */
    public static final String ORDER_DISTRIBUTOR_SHOP_DISABLED_ERROR = "ORDER_DISTRIBUTOR_SHOP_DISABLED_ERROR";

    /**
     * 分账业务员已被停用
     */
    public static final String ORDER_DISTRIBUTOR_SALEMAN_DISABLED_ERROR = "ORDER_DISTRIBUTOR_SALEMAN_DISABLED_ERROR";

    /**
     * 订单归属分销商费用数据为空、无法进行分账、请稍后重试
     */
    public static final String ORDER_SUB_ACCOUNT_FAIL_BY_DISTRIBUTION_COST_NULL =
        "ORDER_SUB_ACCOUNT_FAIL_BY_DISTRIBUTION_COST_NULL";
}
