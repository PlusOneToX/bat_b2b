package com.bat.goods.service.goods.executor;

public class ErrorCode {

    // 商品不存在
    public static final String B_GOODS_NUll = "B_GOODS_NULL";
    /** 货品不存在 */
    public static final String B_GOODS_ITEM_NUll = "B_GOODS_ITEM_NUll";
    // 商品冻结必需先下架
    public static final String B_GOODS_FREEZE_STATUS_ERROR = "B_GOODS_FREEZE_STATUS_ERROR";
    // 商品删除必需先冻结
    public static final String B_GOODS_DELETE_ERROR = "B_GOODS_DELETE_ERROR";

    public static final String B_GOODS_RPC_COLUMN_GOODS_ERROR = "B_GOODS_RPC_COLUMN_GOODS_ERROR";

    public static final String B_GOODS_RPC_SECTION_GOODS_ERROR = "B_GOODS_RPC_SECTION_GOODS_ERROR";

    public static final String B_GOODS_RPC_MOBILE_GOODS_ERROR = "B_GOODS_RPC_MOBILE_GOODS_ERROR";

    public static final String P_GOODS_SKU_NULL = "P_GOODS_SKU_NULL";

    public static final String B_GOODS_DISTRIBUTOR_CONTROL_NULL = "B_GOODS_DISTRIBUTOR_CONTROL_NULL";

    public static final String B_GOODS_SYNCING_PRICE = "B_GOODS_SYNCING_PRICE";

    public static final String B_GOODS_SYNCING_BOX = "B_GOODS_SYNCING_BOX";

    public static final String B_GOODS_SYNCING_INFO = "B_GOODS_SYNCING_INFO";

    public static final String B_GOODS_SYNCING_SCOPE = "B_GOODS_SYNCING_SCOPE";

    public static final String B_GOODS_DISTRIBUTOR_SCOPE_ERROR = "B_GOODS_DISTRIBUTOR_SCOPE_ERROR";

    public static final String B_GOODS_DISTRIBUTOR_SCOPE_ALL_ERROR = "B_GOODS_DISTRIBUTOR_SCOPE_ALL_ERROR";

}
