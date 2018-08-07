package com.bat.warehouse.manager.common.error;

public class WarehouseInStockErrorCode {

    // 销售区域id列表和仓库id列表不能同时为空
    public final static String W_AREAIDLIST_AND_WAREHOUSEIDLIST_NULL = "W_AREAIDLIST_AND_WAREHOUSEIDLIST_NULL";

    // 分销商尚未关联销售区域、请与客服沟通
    public final static String W_DISTRIBUTOR_SALE_AREA_NULL = "W_DISTRIBUTOR_SALE_AREA_NULL";

    // 分销商尚未配置可用在库仓库、请与客服沟通
    public final static String W_DISTRIBUTOR_RELEVANCE_WAREHOUSE_NULL = "W_DISTRIBUTOR_RELEVANCE_WAREHOUSE_NULL";

    // 设置库存预留异常
    public final static String W_WAREHOUSE_STOCK_RESERVED_SET_ERROR = "W_WAREHOUSE_STOCK_RESERVED_SET_ERROR";

    /**
     * 货品何仓库关系错误
     */
    public final static String W_WAREHOUSE_STOCK_ITEM_RELEVANCE_ERROR = "W_WAREHOUSE_STOCK_ITEM_RELEVANCE_ERROR";

    /**
     * 同步ERP库存异常
     */
    public final static String W_WAREHOUSE_STOCK_SYNC_ERP_EXCEPTION = "W_WAREHOUSE_STOCK_SYNC_ERP_EXCEPTION";

    /**
     * 其他人正在全量同步ERP库存、请勿重复操做
     */
    public final static String W_WAREHOUSE_STOCK_SYNC_ERP_REPEAT = "W_WAREHOUSE_STOCK_SYNC_ERP_REPEAT";

    /**
     * 其他人正在初始化库存
     */
    public final static String W_WAREHOUSE_INIT_STOCK_SYNC = "W_WAREHOUSE_INIT_STOCK_SYNC";

    /**
     * 初始化库存失败
     */
    public static final String W_WAREHOUSE_STOCK_INIT_EXCEPTION = "W_WAREHOUSE_STOCK_INIT_EXCEPTION";
}
