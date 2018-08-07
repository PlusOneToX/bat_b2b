package com.bat.warehouse.manager.common.error;

public class WarehouseErrorCode {

    // 仓库已被删除
    public final static String W_DEL_FLAG_YES = "W_DEL_FLAG_YES";

    // 仓库已被删除
    public final static String W_UP_OR_DOWN_NULL = "W_UP_OR_DOWN_NULL";

    // 已是最高、请勿上移
    public final static String W_UP_TOP_ERROR = "W_UP_TOP_ERROR";

    // 已是最低、请勿下移
    public final static String W_DOWN_LOWEST_ERROR = "W_DOWN_LOWEST_ERROR";

    // 仓库编码错误
    public final static String W_WAREHOUSE_NO_ERROR = "W_WAREHOUSE_NO_ERROR";

    // 没有可用的仓库
    public final static String W_WAREHOUSE_NONE_ENABLE_ERROR = "W_WAREHOUSE_NONE_ENABLE_ERROR";

    // 仓库id和仓库编码不能同时为空
    public final static String W_ID_AND_WAREHOUSE_NO_ALL_NULL = "W_ID_AND_WAREHOUSE_NO_ALL_NULL";

    // 销售区域id列表不能为空
    public static final String W_AREA_ID_LIST_NULL = "W_AREA_ID_LIST_NULL";

    // 库存同步数量错误
    public final static String W_WAREHOUSE_SYNC_STOCK_NUM_ERROR = "W_WAREHOUSE_SYNC_STOCK_NUM_ERROR";
}
