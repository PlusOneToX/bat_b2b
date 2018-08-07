package com.bat.order.service.common.constant;

public class OrderDeliverConstant {

    /**
     * 发货状态 1、待确认 2、已确认 3、已取消
     */
    public static final Short ORDER_DELIVER_STATUS_UN_CONFIRMED = 1;

    public static final Short ORDER_DELIVER_STATUS_CONFIRMED = 2;

    public static final Short ORDER_DELIVER_STATUS_CANCEL = 3;

    /**
     * 物流状态：2-在途中,3-签收,4-问题件
     */
    public static final Short ORDER_DELIVER_LOGISTICS_STATUS_ON_WAY = 2;

    public static final Short ORDER_DELIVER_LOGISTICS_STATUS_SIGN = 3;

    public static final Short ORDER_DELIVER_LOGISTICS_STATUS_PROBLEM = 4;

    // 订单发货类型 1、销售单出库 2、换货

    public final static Short ORDER_DELIVER_TYPE_SALE = 1;

    public final static Short ORDER_DELIVER_TYPE_CHANGE = 2;

    // 是否需要推送第三方1、是 0、否
    public final static Short ORDER_DELIVER_NEED_PUSH_YES = 1;

    public final static Short ORDER_DELIVER_NEED_PUSH_NO = 0;

    // 同步给第三方成功与否: 1为同步成功，0为同步失败
    public final static Short ORDER_DELIVER_PUSH_STATUS_SUCCESS = 1;

    public final static Short ORDER_DELIVER_PUSH_STATUS_FAIL = 0;

    // ERP同步出库单到B2B DocumentStatus 单据状态 1.创建，2.已审核，3.取消，4.删除 5.提交
    public final static Short ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_CREATE = 1;

    public final static Short ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_AUDIT = 2;

    public final static Short ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_CANCEL = 3;

    public final static Short ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_DELETE = 4;

    public final static Short ORDER_DELIVER_ERP_SYNC_OUTBOUND_DOCUMENTSTATUS_SUBMIT = 5;
}
