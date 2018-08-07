package com.bat.order.service.common.constant;

public class OrderInfoConstant {

    /**
     * 发货状态 1.未发货2.出库中 3.部分发货 4.已发货
     */
    public static final Short ORDER_DELIVER_STATUS_UN_SHIPPED = 1;

    public static final String ORDER_DELIVER_STATUS_UN_SHIPPED_NAME = "未发货";

    public static final Short ORDER_DELIVER_STATUS_OUTBOUNDING = 2;

    public static final String ORDER_DELIVER_STATUS_OUTBOUNDING_NAME = "出库中";

    public static final Short ORDER_DELIVER_STATUS_SOME_SHIPPED = 3;

    public static final String ORDER_DELIVER_STATUS_SOME_SHIPPED_NAME = "部分发货";

    public static final Short ORDER_DELIVER_STATUS_SHIPPED = 4;

    public static final String ORDER_DELIVER_STATUS_SHIPPED_NAME = "已发货";
    /**
     * 库存类型：1在库 2在途 3预售(mto) 4 在途+在库 5、委外（DIY定制）
     */
    public static final Short ORDER_STOCK_TYPE_INSTOCK = 1;

    public static final Short ORDER_STOCK_TYPE_ONWAY = 2;

    public static final Short ORDER_STOCK_TYPE_MTO = 3;

    public static final Short ORDER_STOCK_TYPE_MERGE = 4;

    public static final Short ORDER_STOCK_TYPE_OUTSOURCE = 5;

    /**
     * 是否开具发票
     */
    public static final Short ORDER_INVOICE_FLAG_YES = 1;

    public static final Short ORDER_INVOICE_FLAG_NO = 0;

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

    /**
     * 是否同步到erp 1.是 0.否
     */
    public static final Short ORDER_ERP_FLAG_YES = 1;

    public static final Short ORDER_ERP_FLAG_NO = 0;

    // 付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款
    public static final Short ORDER_PAY_STATUS_UNPAY = 1;

    public static final String ORDER_PAY_STATUS_UNPAY_NAME = "未付款";

    public static final Short ORDER_PAY_STATUS_PARTIAL_PAYMENT = 2;

    public static final String ORDER_PAY_STATUS_PARTIAL_PAYMENT_NAME = "部分付款";

    public static final Short ORDER_PAY_STATUS_PAID = 3;

    public static final String ORDER_PAY_STATUS_PAID_NAME = "已付款";

    public static final Short ORDER_PAY_STATUS_REBATES = 4;

    public static final String ORDER_PAY_STATUS_REBATES_NAME = "部分退款";

    public static final Short ORDER_PAY_STATUS_REFUND_APPLY = 5;

    public static final String ORDER_PAY_STATUS_REFUND_APPLY_NAME = "退款申请中";

    public static final Short ORDER_PAY_STATUS_REFUNDED = 6;

    public static final String ORDER_PAY_STATUS_REFUNDED_NAME = "已退款";

    /**
     * 柔性出库单是否同步：1 同步 0 不同步
     */
    public static final Short ERP_PURCHASE_0 = 0;
    public static final Short ERP_PURCHASE_1 = 1;
}
