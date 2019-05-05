package com.bat.thirdparty.common.order;

public class OrderInfoConstant {

    /**
     * 发货状态 1.未发货2.出库中 3.部分发货 4.已发货
     */
    public static final Short ORDER_DELIVER_STATUS_UN_SHIPPED=1;

    public static final Short ORDER_DELIVER_STATUS_OUTBOUNDING=2;

    public static final Short ORDER_DELIVER_STATUS_SOME_SHIPPED=3;

    public static final Short ORDER_DELIVER_STATUS_SHIPPED=4;

    /**
     * 库存类型：1在库 2在途 3预售(mto)  4 在途+在库 5、委外（DIY定制）
     */
    public static final Short ORDER_STOCK_TYPE_INSTOCK=1;

    public static final Short ORDER_STOCK_TYPE_ONWAY=2;

    public static final Short ORDER_STOCK_TYPE_MTO=3;

    public static final Short ORDER_STOCK_TYPE_MERGE=4;

    public static final Short ORDER_STOCK_TYPE_OUTSOURCE=5;


    /**
     * 是否开具发票
     */
    public static final Short ORDER_INVOICE_FLAG_YES=1;

    public static final Short ORDER_INVOICE_FLAG_NO=0;

    /**
     * 订单状态 1.待确认2.已确认 4.已取消 5.已完成
     */
    public static final Short ORDER_STATUS_UN_CONFIRM=1;

    public static final Short ORDER_STATUS_CONFIRMED=2;

    public static final Short ORDER_STATUS_CANCEL=4;

    public static final Short ORDER_STATUS_COMPLETED=5;

    // 自动下推出库 1.是(直发) 0.否
    public static final Short ORDER_INFO_AUTO_DELIVERY_YES=1;
    public static final Short ORDER_INFO_AUTO_DELIVERY_NOT=0;

    //是否同步到erp 1.是 0.否
    public static final Short ORDER_DISTRIBUTOR_SYNC_ERP_FLAG_YES=1;

    public static final Short ORDER_DISTRIBUTOR_SYNC_ERP_FLAG_NO=0;

    //取消订单类型 1、作废 2、取消
    public static final Short ORDER_OPERATE_TYPE_INVALID=1;

    public static final Short ORDER_OPERATE_TYPE_CANCEL=2;

    //付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款
    public static final Short ORDER_PAY_STATUS_UNPAY=1;

    public static final Short ORDER_PAY_STATUS_PARTIAL_PAYMENT=2;

    public static final Short ORDER_PAY_STATUS_PAID=3;

    public static final Short ORDER_PAY_STATUS_REBATES=4;

    public static final Short ORDER_PAY_STATUS_REFUND_APPLY=5;

    public static final Short ORDER_PAY_STATUS_REFUNDED=6;

    //'是否直接下单 1.是认0.否'
    public static final Short ORDER_DISTRIBUTOR_DATA_DIRECT_FLAG_YES=1;

    public static final Short ORDER_DISTRIBUTOR_DATA_DIRECT_FLAG_NO=0;
}
