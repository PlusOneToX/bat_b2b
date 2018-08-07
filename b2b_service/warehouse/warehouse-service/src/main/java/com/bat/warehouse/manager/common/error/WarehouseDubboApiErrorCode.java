package com.bat.warehouse.manager.common.error;

public class WarehouseDubboApiErrorCode {

    /**
     * 库存类型为空
     */
    public static final String STOCK_TYPE_NULL = "STOCK_TYPE_NULL";

    /**
     * 扣减库存货品数量列表不能为空
     */
    public static final String STOCK_COUNT_LIST_NULL = "STOCK_COUNT_LIST_NULL";

    /**
     * 订单类型值不能为空
     */
    public static final String ORDER_TYPE_VALUE_NULL = "ORDER_TYPE_VALUE_NULL";

    /**
     * 是否拆单不能为空
     */
    public static final String SPLIT_FLAG_NULL = "SPLIT_FLAG_NULL";

    /**
     * 是否库存充足不能为空
     */
    public static final String IS_INVENTORY_ENOUGH_NULL = "IS_INVENTORY_ENOUGH_NULL";

    /**
     * 错误提示货品编码 【8019782013】
     */
    public static final String ERROR_MSG_ITEM_CODE = "ERROR_MSG_ITEM_CODE";

    /**
     * 在库库存不足
     */
    public static final String IN_STOCK_INVENTORY_INSUFFICIENT = "IN_STOCK_INVENTORY_INSUFFICIENT";

    /**
     * 锁库货品列表不能为空
     */
    public static final String W_ITEM_LOCK_STOCK_LIST_NULL = "W_ITEM_LOCK_STOCK_LIST_NULL";

    /**
     * 货品ID列表不能为空
     */
    public static final String W_ITEM_ID_NULL = "W_ITEM_ID_NULL";

    /**
     * 分销商ID和销售区域Id列表不能同时为空
     */
    public static final String W_DISTRIBUTOR_ID_AND_AREA_ID_LIST_ALL_NULL =
        "W_DISTRIBUTOR_ID_AND_AREA_ID_LIST_ALL_NULL";

    /**
     * 在库下单量不能为空
     */
    public static final String W_ITEM_IN_STOCK_LOCK_COUNT_NULL = "W_ITEM_IN_STOCK_LOCK_COUNT_NULL";

    /**
     * 在途下单量不能为空
     */
    public static final String W_ITEM_ON_WAY_LOCK_COUNT_NULL = "W_ITEM_ON_WAY_LOCK_COUNT_NULL";

    /**
     * 数量非法
     */
    public static final String W_COMMON_QUANTITY_ILLEGAL = "W_COMMON_QUANTITY_ILLEGAL";

    /**
     * 在库下单量和在途下单量不能同时为0
     */
    public static final String W_ITEM_IN_STOCK_LOCK_COUNT_AND_ON_WAY_LOCK_COUNT_ALL_ZERO =
        "W_ITEM_IN_STOCK_LOCK_COUNT_AND_ON_WAY_LOCK_COUNT_ALL_ZERO";

    /**
     * 库存解锁列表不能为空
     */
    public static final String W_ITEM_UN_LOCK_LIST_NULL = "W_ITEM_UN_LOCK_LIST_NULL";

    /**
     * 库存解锁库存对象不能全为空
     */
    public static final String W_ITEM_UN_LOCK_STOCK_NULL = "W_ITEM_UN_LOCK_STOCK_NULL";

    public static final String W_ITEM_UN_LOCK_STOCK_ERROR = "W_ITEM_UN_LOCK_STOCK_ERROR";

    /**
     * ERP库存变更明细列表不能为空
     */
    public static final String W_ERP_STOCK_CHANGE_LIST_NULL = "W_ERP_STOCK_CHANGE_LIST_NULL";

    /**
     * ERP订单号不能为空
     */
    public static final String W_ERP_ORDER_NO_NULL = "W_ERP_ORDER_NO_NULL";

    /**
     * 货品编码不能为空
     */
    public static final String W_ITEM_CODE_NULL = "W_ITEM_CODE_NULL";

    /**
     * ERP销售订单明细内码不能为空
     */
    public static final String W_ERP_ORDER_DETAIL_CODE_NULL = "W_ERP_ORDER_DETAIL_CODE_NULL";

    /**
     * 不支持在途库存、但在途下单数量不为0
     */
    public static final String W_ITEM_NOT_SUPPORT_ON_WAY = "W_ITEM_NOT_SUPPORT_ON_WAY";

    /**
     * 锁库列表和解锁列表不能都为空
     */
    public static final String W_LOCK_STOCK_LIST_AND_UN_LOCK_LIST_ALL_NULL =
        "W_LOCK_STOCK_LIST_AND_UN_LOCK_LIST_ALL_NULL";

    /**
     * 库存变更异常
     */
    public static final String W_WAREHOUSE_STOCK_CHANGE_EXCEPTION = "W_WAREHOUSE_STOCK_CHANGE_EXCEPTION";
}
