package com.bat.order.service.common.error;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/4 16:14
 */
public class ImportOrderErrorCode {

    /**
     * 手机号错误
     */
    public static final String B_FILE_IS_EMPTY = "B_FILE_IS_EMPTY";

    /**
     * 手机号错误
     */
    public static final String ADDRESS_MOBILE_NULL = "ADDRESS_MOBILE_NULL";

    /**
     * 付款方式错误
     */
    public static final String B_ORDER_PAY_WAY_ERROR = "B_ORDER_PAY_WAY_ERROR";

    /**
     * 配送方式错误
     */
    public static final String B_ORDER_LOGISTICS_ERROR = "B_ORDER_LOGISTICS_ERROR";

    /**
     * 在途拆单标志错误
     */
    public static final String B_ORDER_ON_WAY_SPLIT_FLAG_ERROR = "B_ORDER_ON_WAY_SPLIT_FLAG_ERROR";

    /**
     * 行存货编码在系统中不存在
     */
    public static final String B_GOODS_ITEM_IS_NOT_EXITS = "B_GOODS_ITEM_IS_NOT_EXITS";

    /**
     * 行存货编码对应的商品在系统中不存在
     */
    public static final String B_GOODS_ITEM_CORRESPONDING_GOODS_IS_NOT_EXITS =
        "B_GOODS_ITEM_CORRESPONDING_GOODS_IS_NOT_EXITS";

    /**
     * 行存货编码对应的商品数量错误
     */
    public static final String B_GOODS_ITEM_CORRESPONDING_GOODS_IS_NUM_ERROR =
        "B_GOODS_ITEM_CORRESPONDING_GOODS_IS_NUM_ERROR";

    /**
     * 货品非在售状态
     */
    public static final String B_GOODS_ITEM_STATUS_NOT_ON_SALE = "B_GOODS_ITEM_STATUS_NOT_ON_SALE";

    /**
     * 货品该分销商不可视
     */
    public static final String B_GOODS_ITEM_DISTRIBUTOR_SCOPE_NULL = "B_GOODS_ITEM_DISTRIBUTOR_SCOPE_NULL";

    /**
     * 货品该分销商不可视
     */
    public static final String B_GOODS_ITEM_NOT_SUPPORT_ADVANCE_SALE = "B_GOODS_ITEM_NOT_SUPPORT_ADVANCE_SALE";

    /**
     * 分销商不支持区间结算
     */
    public static final String B_DISTRIBUTOR_NOT_SUPPORT_INTERVAL_SETTLEMENT =
        "B_DISTRIBUTOR_NOT_SUPPORT_INTERVAL_SETTLEMENT";

    /**
     * 余额付款不支持
     */
    public static final String B_BALANCE_NOT_SUPPORT = "B_BALANCE_NOT_SUPPORT";

    /**
     * 行存货编码对应的商品不支持预售
     */
    public static final String B_PRE_SALE_GOODS_ITEM_NOT_SUPPORT_PRE_SALE_MODULE =
        "B_PRE_SALE_GOODS_ITEM_NOT_SUPPORT_PRE_SALE_MODULE";

    /**
     * 行存货编码对应的商品已停产
     */
    public static final String B_PRE_SALE_GOODS_ITEM_HAS_BEEN_DISCONTINUED =
        "B_PRE_SALE_GOODS_ITEM_HAS_BEEN_DISCONTINUED";

    /**
     * 行存货编码对应的商品设置的预售数量不合规
     */
    public static final String B_PRE_SALE_GOODS_ITEM_MOQ_ILLEGAL = "B_PRE_SALE_GOODS_ITEM_MOQ_ILLEGAL";

    /**
     * 行购买货品不满足MOQ
     */
    public static final String B_ITEM_COUNT_LESS_THAN_MOQ = "B_ITEM_COUNT_LESS_THAN_MOQ";

    /**
     * 行购买货品不满足MOQ
     */
    public static final String B_ITEM_COUNT_MORE_THAN_100_MILLION = "B_ITEM_COUNT_MORE_THAN_100_MILLION";

    /**
     * 普通商品定制商品预售商品不允许同时下单
     */
    public static final String B_STANDARD_GOODS_DIY_GOODS_PRE_SALE_GOODS_YOU_CANNOT_PLACE_ORDERS_AT_THE_SAME_TIME =
        "B_STANDARD_GOODS_DIY_GOODS_PRE_SALE_GOODS_YOU_CANNOT_PLACE_ORDERS_AT_THE_SAME_TIME";

    /**
     * 订单序号
     */
    public static final String B_ORDER_SERIAL_NUMBER = "B_ORDER_SERIAL_NUMBER";

    /**
     * 货品数据为空
     */
    public static final String B_ORDER_GOODS_ITEM_DATA_EMPTY = "B_ORDER_GOODS_ITEM_DATA_EMPTY";

    /**
     * 订单类型与货品支持的订单类型不一致
     */
    public static final String B_ORDER_TYPE_IS_INCONSISTENT_WITH_THE_GOODS_ITEM_TYPE =
        "B_ORDER_TYPE_IS_INCONSISTENT_WITH_THE_GOODS_ITEM_TYPE";

    /**
     * 分销商销售区域为空
     */
    public static final String B_DISTRIBUTOR_AREA_IS_NULL = "B_DISTRIBUTOR_AREA_IS_NULL";

    /**
     * 配送方式为空或部分订单类型配送方式为空
     */
    public static final String B_ORDER_LOGISTICS_NULL = "B_ORDER_LOGISTICS_NULL";

    /**
     * 分销商与订单对应关系错误
     */
    public static final String B_ORDER_NOT_BELONG_TO_DISTRIBUTOR = "B_ORDER_NOT_BELONG_TO_DISTRIBUTOR";

    /**
     * 导入订单正在提交中或已提交成功，不能重复提交或修改
     */
    public static final String B_IMPORT_SUBMIT_STATUS_ERROR = "B_IMPORT_SUBMIT_STATUS_ERROR";

    /**
     * 请选择配送地区国家
     */
    public static final String ADDRESS_COUNTRY_NAME_NULL = "ADDRESS_COUNTRY_NAME_NULL";

    /**
     * 请选择配送地区省份
     */
    public static final String ADDRESS_PROVINCE_NAME_NULL = "ADDRESS_PROVINCE_NAME_NULL";

    /**
     * 请选择配送地区城市
     */
    public static final String ADDRESS_CITY_NAME_NULL = "ADDRESS_CITY_NAME_NULL";

    /**
     * 请选择配送地区城市
     */
    public static final String ADDRESS_ADDRESS_NULL = "ADDRESS_ADDRESS_NULL";

    /**
     * 库存数据不存在
     */
    public static final String B_GOODS_STOCK_NULL = "B_GOODS_STOCK_NULL";

    /**
     * 库存数据不存在
     */
    public static final String B_ORDER_ITEM_STOCK_NOT_ENOUGH = "B_ORDER_ITEM_STOCK_NOT_ENOUGH";

    /**
     * 区域不存在
     */
    public static final String B_REGION_NOT_EXITS = "B_REGION_NOT_EXITS";

    /**
     * 分销商 不存在或 状态 不可用
     */
    public static final String B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE = "B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE";

}
