package com.bat.thirdparty.common.error.quanyi;

public class QuanyiErrorCode {

    /**
     * 权益已取消；不能重复取消
     */
    public static final String QUANYI_HAS_CANCEL = "QUANYI_HAS_CANCEL";

    /**
     * 权益已核销，不能取消
     */
    public static final String QUANYI_HAS_DESTROY_CANNOT_CANCEL = "QUANYI_HAS_DESTROY_CANNOT_CANCEL";

    /**
     * 权益核销失败
     */
    public static final String QUANYI_DESTROY_ERROR = "QUANYI_DESTROY_ERROR";

    /**
     * 权益已领取；不能重复领取
     */
    public static final String QUANYI_HAS_GET = "QUANYI_HAS_GET";

    /**
     * 权益签到失败
     */
    public static final String QUANYI_SIGN_ERROR = "QUANYI_SIGN_ERROR";

    /**
     * 订单创建失败
     */
    public static final String QUANYI_ORDER_CREATE_ERROR = "QUANYI_ORDER_CREATE_ERROR";
}
