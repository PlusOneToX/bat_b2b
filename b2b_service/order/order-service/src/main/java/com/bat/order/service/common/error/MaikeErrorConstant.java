package com.bat.order.service.common.error;

public class MaikeErrorConstant {

    public final static int ProductAddErrorCode = 60001;

    public final static int OrderAddErrorCode = 60002;

    public final static int SyncOrderNoNullCode = 60003;
    public final static String SyncOrderNoNullMsg = "订单编号不能为空";

    public final static Integer DeliverySynOrderNullCode = 60004;
    public final static String DeliverySynOrderNullMsg = "找不到对应订单，订单不存在";

    public final static Integer DeliverySynFailCode = 60005;
    public final static String DeliverySynFailMsg = "订单发货失败";

    public final static Integer CancelOrderFailCode = 60006;
    public final static String CancelOrderFailMsg = "工厂取消订单失败:";
}
