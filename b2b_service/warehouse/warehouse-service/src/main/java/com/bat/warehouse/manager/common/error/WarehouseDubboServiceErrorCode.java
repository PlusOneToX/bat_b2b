package com.bat.warehouse.manager.common.error;

/**
 * dubbo接口访问异常
 */
public class WarehouseDubboServiceErrorCode {

    /**
     * 访问商品服务异常
     */
    public static final String DUBBO_GOODS_SERVICE_EXCEPTION = "DUBBO_GOODS_SERVICE_EXCEPTION";

    /**
     * 访问分销商服务异常
     */
    public static final String DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION = "DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION";

    /**
     * 访问订单服务异常
     */
    public static final String DUBBO_ORDER_SERVICE_EXCEPTION = "DUBBO_ORDER_SERVICE_EXCEPTION";

    /**
     * 访问柔性服务异常
     */
    public static final String DUBBO_FLEXIBLE_SERVICE_EXCEPTION = "DUBBO_FLEXIBLE_SERVICE_EXCEPTION";

    /**
     * 删除库存失败
     */
    public static final String DUBBO_WAREHOUSE_STOCK_DELETE_ERROR = "DUBBO_WAREHOUSE_STOCK_DELETE_ERROR";
}
