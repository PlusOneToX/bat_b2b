package com.bat.thirdparty.common.order;

public class OrderDeliveryConstant {

    //同步给第三方成功与否: 1为同步成功，0为同步失败，3、不允许修改订单物流单号
    public static final Short ORDER_DELIVERY_STATUS_SUCCESS=1;

    public static final Short ORDER_DELIVERY_STATUS_FAIL=2;

    public static final Short ORDER_DELIVERY_STATUS_REJECT=3;

    //配送方式类型：1 普通商品（标品） 2 定制工厂
    public static final Short ORDER_LOGISTRICS_TYPE_GENERAL=1;

    public static final Short ORDER_LOGISTRICS_TYPE_DIY=2;
}
