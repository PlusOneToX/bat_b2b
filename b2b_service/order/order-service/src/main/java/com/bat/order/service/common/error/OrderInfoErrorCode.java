package com.bat.order.service.common.error;

public class OrderInfoErrorCode {

    /**
     * 货品建议零售价为空
     */
    public static final String ORDER_ITEM_RETAIL_PRICE_NULL = "ORDER_ITEM_RETAIL_PRICE_NULL";

    /**
     * 货品实际价格不匹配
     */
    public static final String ORDER_ITEM_ACTUAL_PRICE_ERROR = "ORDER_ITEM_ACTUAL_PRICE_ERROR";

    /**
     * 订单实际支付金额不匹配
     */
    public static final String ORDER_PAY_AMOUNT_ERROR = "ORDER_PAY_AMOUNT_ERROR";

    /**
     * 选择开票时、发票信息不能为空
     */
    public static final String ORDER_INVOICE_NULL_CHOOSE_OPEN_INVOICE = "ORDER_INVOICE_NULL_CHOOSE_OPEN_INVOICE";

    /**
     * 选择开票时、发票类型不能为空
     */
    public static final String ORDER_INVOICE_TYPE_NULL = "ORDER_INVOICE_TYPE_NULL";

    /**
     * 选择开票时、发票抬头类型不能为空
     */
    public static final String ORDER_INVOICE_TITLE_TYPE_NULL = "ORDER_INVOICE_TITLE_TYPE_NULL";

    /**
     * 订单已被取消
     */
    public static final String ORDER_STATUS_HAS_BEEN_CANCEL = "ORDER_STATUS_HAS_BEEN_CANCEL";

    /**
     * 订单已完成
     */
    public static final String ORDER_STATUS_HAS_BEEN_COMPLETED = "ORDER_STATUS_HAS_BEEN_COMPLETED";

    /**
     * 订单未确认
     */
    public static final String ORDER_STATUS_HAS_NO_CONFIRM = "=ORDER_STATUS_HAS_NO_CONFIRM";

    /**
     * 该订单已生成工厂单号、请勿重复生成
     */
    public static final String ORDER_OTHER_FACTORYNO_EXIST = "ORDER_OTHER_FACTORYNO_EXIST";

    public static final String B_ORDER_GOODS_INFO_ERROR = "B_ORDER_GOODS_INFO_ERROR";

    public static final String B_ORDER_GOODS_INVENTORY_NULL = "B_ORDER_GOODS_INVENTORY_NULL";

    public static final String B_ORDER_GOODS_INVENTORY_ERROR = "B_ORDER_GOODS_INVENTORY_ERROR";

    public static final String B_ORDER_GOODS_PRICE_ERROR = "B_ORDER_GOODS_PRICE_ERROR";

    public static final String B_ORDER_GOODS_WEIGHT_NULL = "B_ORDER_GOODS_WEIGHT_NULL";

    public static final String B_ORDER_TYPE_DIY_NULL = "B_ORDER_TYPE_DIY_NULL";

    public static final String B_ORDER_TYPE_MTO_NULL = "B_ORDER_TYPE_MTO_NULL";

    public static final String B_ORDER_GOODS_STOCK_NULL = "B_ORDER_GOODS_STOCK_NULL";

    public static final String B_ORDER_GOODS_IN_STOCK_NULL = "B_ORDER_GOODS_IN_STOCK_NULL";

    public static final String B_PROMOTION_SUCCESS = "B_PROMOTION_SUCCESS";
    public static final String B_ORDER_PROMOTION_ERROR = "B_PROMOTION_ERROR";
    public static final String B_ORDER_RATE_NULL = "B_ORDER_RATE_NULL";

    public static final String B_ORDER_ORDER_AMOUNT_ERROR = "B_ORDER_ORDER_AMOUNT_ERROR";
    public static final String B_ORDER_DISTRIBUTION_AMOUNT_ERROR = "B_ORDER_DISTRIBUTION_AMOUNT_ERROR";
    public static final String P_ORDER_LOGISTICS_NULL = "P_ORDER_LOGISTICS_NULL";

    public static final String B_ORDER_EXCHANGE_NULL = "B_ORDER_EXCHANGE_NULL";
    public static final String B_ORDER_NOT_EXIST = "B_ORDER_NOT_EXIST";

    public static final String B_ORDER_ID_ORDER_NO_NULL = "B_ORDER_ID_ORDER_NO_NULL";

    /**
     * 数据不完整
     */
    public static final String B_ORDER_INCOMPLETE_DATA = "B_ORDER_INCOMPLETE_DATA";

    // 工厂单号错误（请勿修改该值、涉及到bat的）
    public static final Integer O_ORDER_FACTORY_NO_ERROR_CODE = 700012;

    public static final String O_ORDER_FACTORY_NO_ERROR_MSG = "O_ORDER_FACTORY_NO_ERROR_MSG";

    /**
     * 订单号错误
     */
    public static final String ORDER_NO_ERROR = "ORDER_NO_ERROR";

    /**
     * 兑换码正在下单、请勿重复提交
     */
    public static final String ORDER_EXCHANGE_SECRET_CODE_EXCHANGING = "ORDER_EXCHANGE_SECRET_CODE_EXCHANGING";

    /**
     * 工厂编码 不存在
     */
    public static final String MANUFACTORS_CODE_NOT_EXIST = "MANUFACTORS_CODE_NOT_EXIST";

    /**
     * 订单不是待发货状态
     */
    public static final Integer THE_ORDER_IS_NOT_IN_PENDING_STATUS = 404405;

    public static final String THE_ORDER_IS_NOT_IN_PENDING_MSG = "THE_ORDER_IS_NOT_IN_PENDING_MSG";

}
