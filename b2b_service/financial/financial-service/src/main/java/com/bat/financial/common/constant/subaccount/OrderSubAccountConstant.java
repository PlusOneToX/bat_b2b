package com.bat.financial.common.constant.subaccount;

public class OrderSubAccountConstant {

    /**
     * 是否存在分账失败 1、是 0、否
     */
    public static final Short ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES = 1;

    public static final Short ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_NO = 0;

    /**
     * 分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账
     */
    public static final Short ORDER_SUB_ACCOUNT_STATUS_CLOSE = 0;

    public static final Short ORDER_SUB_ACCOUNT_STATUS_UN = 1;

    public static final Short ORDER_SUB_ACCOUNT_STATUS_SOME = 2;

    public static final Short ORDER_SUB_ACCOUNT_STATUS_ALL = 3;

    /**
     * 是否分账成功 1、是 0、否
     */
    public static final Short ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_SUCCESS = 1;

    public static final Short ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_FAIL = 0;
}
