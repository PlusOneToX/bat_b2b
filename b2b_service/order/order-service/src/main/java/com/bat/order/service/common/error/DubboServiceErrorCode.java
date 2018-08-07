package com.bat.order.service.common.error;

/**
 * dubbo接口访问异常
 */
public class DubboServiceErrorCode {

    /**
     * 访问商品服务异常
     */
    public static final String DUBBO_GOODS_SERVICE_EXCEPTION = "DUBBO_GOODS_SERVICE_EXCEPTION";

    /**
     * 访问分销商服务异常
     */
    public static final String DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION = "DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION";

    /**
     * 访问柔性服务异常
     */
    public static final String DUBBO_FLEXIBLE_SERVICE_EXCEPTION = "DUBBO_FLEXIBLE_SERVICE_EXCEPTION";

    /**
     * 访问仓库服务异常
     */
    public static final String DUBBO_WAREHOUSE_SERVICE_EXCEPTION = "DUBBO_WAREHOUSE_SERVICE_EXCEPTION";

    /**
     * 访问财务服务异常
     */
    public static final String DUBBO_FINANCIAL_SERVICE_EXCEPTION = "DUBBO_FINANCIAL_SERVICE_EXCEPTION";

    /**
     * 访问系统服务异常
     */
    public static final String DUBBO_SYSTEM_SERVICE_EXCEPTION = "DUBBO_SYSTEM_SERVICE_EXCEPTION";

    /**
     * 访问第三方服务异常
     */
    public static final String DUBBO_THIRD_SERVICE_EXCEPTION = "DUBBO_THIRD_SERVICE_EXCEPTION";

    /**
     * 订单库存解锁失败
     */
    public static final String DUBBO_ORDER_UNLOCK_EXCEPTION = "DUBBO_ORDER_UNLOCK_EXCEPTION";
}
