package com.bat.financial.common.constant.third;

public class OrderConstant {

    /**
     * 订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成
     */
    public static final Short ORDER_STATUS_UN_CONFIRM = 1;

    public static final String ORDER_STATUS_UN_CONFIRM_NAME = "待确认";

    public static final Short ORDER_STATUS_CONFIRMED = 2;

    public static final String ORDER_STATUS_CONFIRMED_NAME = "已确认";

    public static final Short ORDER_STATUS_REJECTED = 3;

    public static final String ORDER_STATUS_REJECTED_NAME = "已拒绝";

    public static final Short ORDER_STATUS_CANCEL = 4;

    public static final String ORDER_STATUS_CANCEL_NAME = "已取消";

    public static final Short ORDER_STATUS_COMPLETED = 5;

    public static final String ORDER_STATUS_COMPLETED_NAME = "已完成";
}
