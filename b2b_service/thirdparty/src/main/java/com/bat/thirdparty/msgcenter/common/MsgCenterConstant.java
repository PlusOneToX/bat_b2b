package com.bat.thirdparty.msgcenter.common;

public class MsgCenterConstant {

    //用户类型 1B2B用户 2.C端用户
    public static final short USR_TYPE_B2B=1;

    //用户类型 1B2B用户 2.C端用户
    public static final short USR_TYPE_CUSTOMER=2;

    //排序方式 1按照是否阅读以及创建时间倒序排序
    public static final short SORT_TYPE_READ_CREATE=1;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_STATUS=1;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_DELIVERY=2;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_UNPAID=3;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_EXAMINE=4;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_NEW=5;

    //消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息
    public static final short MSG_TYPE_CUSTOMIZE=6;

    //订单状态通知-订单状态
    public static final String ORDER_EXAMINE_STATUS ="审核通过";

    //订单状态通知-温馨提示
    public static final String ORDER_STATUS_TIP ="恭喜您，订单已经审核通过。";

    //订单发货提醒-温馨提示
    public static final String ORDER_DELIVERY_TIP ="您的订单已发货，点击查看物流详情。";

    //订单未支付提醒-温馨提示
    public static final String ORDER_UNPAID_TIP ="您的订单还未完成支付，请及时支付，如已支付请忽略。";

    //分销商审核通知-温馨提示
    public static final String DISTRIBUTOR_EXAMINE_TIP ="请尽快审核";

    // 消息渠道 1.B2b 2.兑换商城 3.定制商城
    public static final short CHANNEL_1=1;
    public static final short CHANNEL_2=2;
    public static final short CHANNEL_3=3;

    //推送终端 1短信 2微信B2B小程序 3微信定制小程序4.抖音定制小程序
    public static final short PUSH_TERMINAL_1=1;
    public static final short PUSH_TERMINAL_2=2;
    public static final short PUSH_TERMINAL_3=3;
    public static final short PUSH_TERMINAL_4=4;

    /**
     * 分销商推送范围 1全部 2分销商等级 3.指定分销商 4.指定销售部门 5.指定业务员 6.指定分销商分组
     */
    public static final short SCOPE_ALL = 1;
    public static final Short SCOPE_SCALE_PRICE = 2;
    public static final Short SCOPE_DISTRIBUTOR = 3;
    public static final Short SCOPE_DEPARTMENT = 4;
    public static final Short SCOPE_ADMIN = 5;
    public static final Short SCOPE_DISTRIBUTOR_GROUP = 6;


    /**
     * 用户类型 1B2B用户 2.C端用户
     */
    public static final short USER_TYPE_1=1;
    public static final short USER_TYPE_2=2;

    /**
     * 小程序订阅标题
     */
    public static final String MSG_TITLE="微信消息推送";
}
