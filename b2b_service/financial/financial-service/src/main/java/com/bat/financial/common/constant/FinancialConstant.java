package com.bat.financial.common.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/05/12 21:27
 */
public class FinancialConstant {
    public static final String REFUND_TRY_LOCK = "REFUND_TRY_LOCK";

    /**
     * 服务模块：8-财务服务
     */
    public static final Short MODEL_TYPE8 = 8;

    /**
     * 订单分账数据生成处理分布式锁
     */
    public static final String ORDER_SUB_ACCOUNT_CREATE_LOCK_KEY = "FINANCIAL:ORDER_SUB_ACCOUNT_CREATE_LOCK_KEY:";

    /**
     * 微信服务商分账生成处理分布式锁()
     */
    public static final String WECHAT_PARTNER_SUB_ACCOUNT_BILL_LOCK_KEY =
        "FINANCIAL:WECHAT_PARTNER_SUB_ACCOUNT_BILL_LOCK_KEY:";
    /**
     * url类型：1-后台 2-前台pc端 3-前台h5端4-店铺二维码 5-分销商申请二维码 6-后端接口baseurl
     */
    public static final Short URL_TYPE_6 = 6;

    /**
     * 通用 请勿重复操作
     */
    public static final String COMMON_OPERATE_REPEAT = "COMMON_OPERATE_REPEAT";

    /**
     * 订单分账数据生成处理分布式锁
     */
    public static final String DEPOSIT_SYNC_LOCK_KEY = "FINANCIAL:DEPOSIT_SYNC_LOCK_KEY:";
}
