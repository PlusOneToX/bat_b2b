package com.bat.financial.common.error.subaccount;

public class SubAccountErrorCode {

    /**
     * 订单已关闭
     */
    public static final String ORDER_STATUS_CLOSE = "ORDER_STATUS_CLOSE";

    /**
     * 订单分账已关闭
     */
    public static final String ORDER_SUB_ACCOUNT_STATUS_CLOSE = "ORDER_SUB_ACCOUNT_STATUS_CLOSE";

    /**
     * 订单分账流水已关闭
     */
    public static final String BILL_SUB_ACCOUNT_STATUS_CLOSE = "BILL_SUB_ACCOUNT_STATUS_CLOSE";

    /**
     * 订单分账流水已分账、请勿重复操作
     */
    public static final String BILL_SUB_ACCOUNT_STATUS_SUCCESS = "BILL_SUB_ACCOUNT_STATUS_SUCCESS";

    /**
     * 订单已分账、请勿重复操作
     */
    public static final String ORDER_SUB_ACCOUNT_STATUS_SUCCESS = "ORDER_SUB_ACCOUNT_STATUS_SUCCESS";

    /**
     * 分账金额超过预定金额
     */
    public static final String ORDER_SUB_ACCOUNT_AMOUNT_EXCESS_ERROR = "ORDER_SUB_ACCOUNT_AMOUNT_EXCESS_ERROR";

    /**
     * 已分账的不允许修改金额
     */
    public static final String ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_STATUS_SUCCESS =
        "ORDER_SUB_ACCOUNT_AMOUNT_EXCESS_ERROR";

    /**
     * 已关闭的不允许修改金额
     */
    public static final String ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_STATUS_CLOSE =
        "ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_STATUS_CLOSE";

    /**
     * 修改金额不能超过一个订单
     */
    public static final String ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_MORE_THAN_ONE_ORDER =
        "ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_MORE_THAN_ONE_ORDER";

    /**
     * 流水的没有业务员
     */
    public static final String ORDER_SUB_ACCOUNT_BILL_SALE_MAN_ID_NULL = "ORDER_SUB_ACCOUNT_BILL_SALE_MAN_ID_NULL";

}
