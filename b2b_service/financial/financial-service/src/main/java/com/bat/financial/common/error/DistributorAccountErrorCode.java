package com.bat.financial.common.error;

/**
 * 分销商账户错误常量类
 */
public class DistributorAccountErrorCode {

    /**
     * 账户类型为服务商二级时、最大分账比例不能为空
     */
    public static final String P_DISTRIBUTOR_ACCOUNT_SUB_ACCOUNT_RATIO_NULL =
        "P_DISTRIBUTOR_ACCOUNT_SUB_ACCOUNT_RATIO_NULL";

    /**
     * 分销商微信支付账户尚未配置、请与客服沟通处理
     */
    public static final String P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL = "P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL";
}
