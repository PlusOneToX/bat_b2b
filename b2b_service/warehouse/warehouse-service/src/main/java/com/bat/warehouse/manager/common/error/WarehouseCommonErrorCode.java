package com.bat.warehouse.manager.common.error;

public class WarehouseCommonErrorCode {
    // 主键id的错误情况
    public final static String ID_ERROR = "ID_ERROR";

    // 主键id为空
    public final static String ID_NULL = "ID_NULL";


    public static final String ITEM_ID_NULL = "ITEM_ID_NULL";
    public static final String ITEM_ID_ERROR = "ITEM_ID_ERROR";

    public static final String ORDER_ID_NULL = "ORDER_ID_NULL";
    /**
     * 数据重复操作
     */
    public static final String COMMON_OPERATE_REPEAT ="COMMON_OPERATE_REPEAT" ;

    /**
     * 分销商id错误
     */
    public static final String COMMON_DISTRIBUTOR_ID_ERROR = "COMMON_DISTRIBUTOR_ID_ERROR";

    /**
     * 货品列表不能为空
     */
    public static final String COMMON_ITEM_LIST_NULL = "COMMON_ITEM_LIST_NULL";

    /**
     * 货品erp id不能为空
     */
    public static final String COMMON_ITEM_ERP_ID_NULL = "COMMON_ITEM_ERP_ID_NULL";

    /**
     * 商品id
     */
    public static final String COMMON_GOODS_ID_NULL = "COMMON_GOODS_ID_NULL";

    /**
     * 系统异常
     */
    public static final String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";

    /**
     * 列表不能为空
     */
    public static final String COMMON_LIST_NULL = "COMMON_LIST_NULL";
}
