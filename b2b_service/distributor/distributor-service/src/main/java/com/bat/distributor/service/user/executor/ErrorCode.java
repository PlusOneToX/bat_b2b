package com.bat.distributor.service.user.executor;

public class ErrorCode {
    /** 账号不存在，请检查输入账号或手机号是否正确 */
    public static final String P_DISTRIBUTOR_USER_NON_EXISTENT = "P_DISTRIBUTOR_USER_NON_EXISTENT";
    /** 登录密码错误 */
    public static final String P_DISTRIBUTOR_USER_PASSWORD_ERROR = "P_DISTRIBUTOR_USER_PASSWORD_ERROR";
    /** 分销商账号密码为空 */
    public static final String B_DISTRIBUTOR_USER_LOGIN_ERROR = "B_DISTRIBUTOR_USER_LOGIN_ERROR";
    /** 分销商账号密码为空 */
    public static final String P_DISTRIBUTOR_CONTACTS_ERROR = "P_DISTRIBUTOR_CONTACTS_ERROR";
    /** 原密码输入错误，请重新输入 */
    public static final String P_DISTRIBUTOR_OLD_PASSWORD_ERROR = "P_DISTRIBUTOR_OLD_PASSWORD_ERROR";
    /** 地址id不存在 */
    public static final String P_DISTRIBUTOR_USER_ADDRESS_ERROR = "P_DISTRIBUTOR_USER_ADDRESS_ERROR";
    /** 您还未开启分销模式 */
    public static final String B_DISTRIBUTOR_USER_NO_DISTRIBUTION_FLAG = "B_DISTRIBUTOR_USER_NO_DISTRIBUTION_FLAG";
    /** 分销商价格等级不存在 */
    public static final String P_DISTRIBUTOR_USER_NEXT_SCALE_PRICE_NULL = "P_DISTRIBUTOR_USER_NEXT_SCALE_PRICE_NULL";

    public static final String B_DISTRIBUTOR_USER_NEXT_ERROR = "B_DISTRIBUTOR_USER_NEXT_ERROR";

    public static final String B_DISTRIBUTOR_USER_BRAND_ERROR = "B_DISTRIBUTOR_USER_BRAND_ERROR";

    public static final String B_DISTRIBUTOR_USER_GOODS_ERROR = "B_DISTRIBUTOR_USER_GOODS_ERROR";

    public static final String B_DISTRIBUTOR_USER_DELETE_NEXT_SCALE_PRICE_ERROR =
        "B_DISTRIBUTOR_USER_DELETE_NEXT_SCALE_PRICE_ERROR";

    public static final String B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_ERROR =
        "B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_ERROR";
    public static final String B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_3_ERROR =
        "B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_3_ERROR";

    /**
     * 用户已被冻结、请与客服沟通处理
     */
    public static final String D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN = "D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN";

    /**
     * 分销商尚未审核通过、请与客服沟通处理
     */
    public static final String D_DISTRIBUTOR_LOGIN_FAIL_APPLY_STATUS_NOT_PASS ="D_DISTRIBUTOR_LOGIN_FAIL_APPLY_STATUS_NOT_PASS" ;
}
