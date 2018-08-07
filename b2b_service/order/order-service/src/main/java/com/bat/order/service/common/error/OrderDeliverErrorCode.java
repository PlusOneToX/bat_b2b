package com.bat.order.service.common.error;

public class OrderDeliverErrorCode {

    /**
     * 订单发货类型错误
     */
    public static final Integer ORDER_DELIVER_TYPE_ERROR_CODE = 100021;

    /**
     * 订单发货类型错误
     */
    public static final String ORDER_DELIVER_TYPE_ERROR_MSG = "ORDER_DELIVER_TYPE_ERROR_MSG";
    /**
     * ERP同步出库单到B2B异常
     */
    public static final Integer ORDER_ERP_SYNC_OUTBOUND_ORDER_EXCEPTION_CODE = 100025;

    /**
     * 订单物流已经成功同步、不需要再次同步
     */
    public static final String ORDER_DELIVER_SYNC_REPEAT = "ORDER_DELIVER_SYNC_REPEAT";

    /**
     * 订单物流不存在
     */
    public static final String B_ORDER_DELIVER_BILL_NOT_EXISTS = "B_ORDER_DELIVER_BILL_NOT_EXISTS";

    /**
     * 出库单和采购单必须要先同步收款单到ERP后再同步
     */
    public static final String ORDER_DELIVER_SYNC_ERP_MUST_AFTER_SYNC_VOUCHER =
        "ORDER_DELIVER_SYNC_ERP_MUST_AFTER_SYNC_VOUCHER";

    /**
     * ERP出库单号错误
     */
    public static final Integer ORDER_ERP_OUTBOUND_ORDER_ERROR_CODE = 100027;

    /**
     * ERP出库单号错误
     */
    public static final String ORDER_ERP_OUTBOUND_ORDER_ERROR_MSG = "ORDER_ERP_OUTBOUND_ORDER_ERROR_MSG";

    /**
     * ERP销售单出库单状态变更错误
     */
    public static final Integer ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_CODE = 100028;

    /**
     * ERP出库单号错误
     */
    public static final String ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_MSG =
        "ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_MSG";

    /**
     * 获取同步ERP销售出库单和采购单参数异常
     */
    public static final String ORDER_ERP_OUTBOUND_ORDER_AND_PURCHASE_PARAM_ERROR =
        "ORDER_ERP_OUTBOUND_ORDER_AND_PURCHASE_PARAM_ERROR";
}
