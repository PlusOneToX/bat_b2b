package com.bat.order.service.common.error;

public class GoodsErrorCode {

    /**
     * 货品id不能为空
     */
    public static final String GOODS_ITEM_ID_NULL = "GOODS_ITEM_ID_NULL";

    /**
     * 货品编码不能为空
     */
    public static final String GOODS_ITEM_CODE_NULL = "GOODS_ITEM_CODE_NULL";

    /**
     * 货品id和货品编码不能都为空
     */
    public static final String GOODS_ITEM_ID_AND_ITEM_CODE_ALL_NULL = "GOODS_ITEM_ID_AND_ITEM_CODE_ALL_NULL";

    /**
     * 货品id错误
     */
    public static final String GOODS_ITEM_ID_ERROR = "GOODS_ITEM_ID_ERROR";

    /**
     * 货品编码错误
     */
    public static final String GOODS_ITEM_CODE_ERROR = "GOODS_ITEM_CODE_ERROR";
}
