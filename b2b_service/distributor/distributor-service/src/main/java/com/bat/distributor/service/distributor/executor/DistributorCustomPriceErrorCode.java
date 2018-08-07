package com.bat.distributor.service.distributor.executor;

/**
 * 分销商定制价格
 */
public class DistributorCustomPriceErrorCode {

    /**新增时、分销商定制价格列表不能为空*/
    public static final String D_DISTRIBUTOR_ITEM_PRICE_LIST_WHEN_ADD_NULL="D_DISTRIBUTOR_ITEM_PRICE_LIST_WHEN_ADD_NULL";

    /**
     * 分销商价格不能为空
     */
    public static final String D_CUSTOM_PRICE_NULL ="D_CUSTOM_PRICE_NULL" ;

    public static final String D_CUSTOM_PRICE_MUST_GREATER_THEN_ONE ="D_CUSTOM_PRICE_MUST_GREATER_THEN_ONE" ;

    public static final String D_ITEM_ID_NULL = "D_ITEM_ID_NULL";

    /**
     * 分销商已设置该货品价格
     */
    public static final String D_CUSTOM_PRICE_ITEM_ALREADY_SET = "D_CUSTOM_PRICE_ITEM_ALREADY_SET";
}
