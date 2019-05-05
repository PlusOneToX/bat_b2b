package com.bat.thirdparty.common;

/**
 * 第三方服务redis缓存key
 */
public class ThirdKeyConstant {

    /**
     * 微信授权令牌 redis前缀 、小程序和公众号都是这里
     */
    public static final String WECHAT_ACCESS_TOKEN_PRE = "THIRD:WECHAT_ACCESS_TOKEN:";

    /**
     * 微信jsapi_ticket redis前缀 、小程序和公众号都是这里
     */
    public static final String WECHAT_JSAPI_TICKET_PRE = "THIRD:WECHAT_JSAPI_TICKET:";

    /**
     * 销售单同步ERP、分布式锁
     */
    public static final String SALE_ORDER_SYNC_ERP_PRE = "THIRD:SALE_ORDER_SYNC_ERP_PRE:";

    /**
     * 销售单同步工厂、分布式锁
     */
    public static final String SALE_ORDER_SYNC_FACTORY_PRE = "THIRD:SALE_ORDER_SYNC_FACTORY_PRE:";

    /**
     * 收款单同步ERP、分布式锁
     */
    public static final String VOUCHER_SYNC_ERP_PRE = "THIRD:SALE_ORDER_SYNC_FACTORY_PRE:";

    /**
     * 同步物流单号
     */
    public static final String EXPRESS_NO_SYNC_ERP_PRE="THIRD:EXPRESS_NO_SYNC_ERP_PRE:";

    /**
     * 同步柔性销售采购单和出库单、分布式锁
     */
    public static final String FLEXIBLE_CUSTOM_DELIBERY_SYNC_ERP_PRE="THIRD:FLEXIBLE_CUSTOM_DELIBERY_SYNC_ERP_PRE:";


    /**
     * ERP登录令牌
     */
    public static final String ERP_LOGIN_TOKEN="THIRD:ERP_LOGIN_TOKEN:";

    /**
     * ERP登录方法分布式锁
     */
    public static final String ERP_LOGIN_KEY="THIRD:ERP_LOGIN_KEY:";

    /**
     * 权益分布式锁
     */
    public static final String EXCHANGE_QUANYI_PRE ="THIRD:EXCHANGE_QUANYI_PRE:";

    /**
     * 苏宁权益下单分布式锁
     */
    public static final String EXCHANGE_SU_NING_ORDER_PRE ="THIRD:EXCHANGE_SU_NING_ORDER_PRE:";
}
