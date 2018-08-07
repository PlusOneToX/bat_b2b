package com.bat.distributor.service.distributor.executor;

public class ErrorCode {
    /** 分销商不存在 */
    public static final String B_DISTRIBUTOR_NULL = "B_DISTRIBUTOR_NULL";
    /** 冻结的分销商才允许删除 */
    public static final String B_DISTRIBUTOR_TRADE_DELETE_FREEZE_APPLY_ERROR =
        "B_DISTRIBUTOR_TRADE_DELETE_FREEZE_APPLY_ERROR";
    /** 分销商已发生业务交易，不能删除 */
    public static final String B_DISTRIBUTOR_DELETE_ERROR = "B_DISTRIBUTOR_DELETE_ERROR";
    /** 至少有一个账号拥有者联系人，且只能有一个账号拥有者联系人 */
    public static final String P_DISTRIBUTOR_CONTACT_OWNER_ERROR = "P_DISTRIBUTOR_CONTACT_OWNER_ERROR";
    /** 分销商用户名已存在，请使用其他名称 */
    public static final String P_DISTRIBUTOR_NAME_ERROR = "P_DISTRIBUTOR_NAME_ERROR";
    /** 分销商联系人电话号码或邮箱已存在，请使用其他联系人电话或邮箱 */
    public static final String P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR = "P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR";
    /** 修改的联系人不存在或id为空 */
    public static final String P_DISTRIBUTOR_CONTACT_NULL = "P_DISTRIBUTOR_CONTACT_NULL";
    /** 修改的特价商品不存在或id为空 */
    public static final String P_DISTRIBUTOR_SPECIAL_GOODS_NULL = "P_DISTRIBUTOR_SPECIAL_GOODS_NULL";
    /** 修改的价格等级不存在或id为空 */
    public static final String P_DISTRIBUTOR_SCALE_PRICE_NULL = "P_DISTRIBUTOR_SCALE_PRICE_NULL";

    public static final String P_DISTRIBUTOR_OLD_PASSWORD_NULL = "P_DISTRIBUTOR_OLD_PASSWORD_NULL";

    public static final String P_DISTRIBUTOR_USER_PHONE_CODE_NULL = "P_DISTRIBUTOR_USER_PHONE_CODE_NULL";

    public static final String P_DISTRIBUTOR_USER_PHONE_NULL = "P_DISTRIBUTOR_USER_PHONE_NULL";

    public static final String B_DISTRIBUTOR_CONTACT_NOT_ALLOW_CHANGE_PASSWORD = "B_DISTRIBUTOR_CONTACT_NOT_ALLOW_CHANGE_PASSWORD";

    public static final String B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_CANNOT_BE_EMPTY_AT_THE_SAME_TIME = "B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_CANNOT_BE_EMPTY_AT_THE_SAME_TIME";

    public static final String B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NOT_EXISTS = "B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NOT_EXISTS";

    public static final String P_DISTRIBUTOR_USER_PHONE_CODE_TYPE_NULL = "P_DISTRIBUTOR_USER_PHONE_CODE_TYPE_NULL";

    /** 分销商联系人原电话号码或邮箱不存在，请确认原电话或邮箱是否正确 */
    public static final String P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NULL = "P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NULL";

    //该账号已被冻结
    public static final String THE_ACCOUNT_HAS_BEEN_FROZEN = "帐户已被冻结";

    /** 获取分销商特价商品失败 */
    public static final String B_DISTRIBUTOR_SPECIAL_GOODS_GET_ERROR = "B_DISTRIBUTOR_SPECIAL_GOODS_GET_ERROR";

    public static final String P_DISTRIBUTOR_PHONE_ERROR = "P_DISTRIBUTOR_PHONE_ERROR";

    public static final String B_DISTRIBUTOR_COMPANY_ADDRESS_NULL = "B_DISTRIBUTOR_COMPANY_ADDRESS_NULL";

    public static final String B_DISTRIBUTOR_CHECK_FLOW_NULL = "B_DISTRIBUTOR_CHECK_FLOW_NULL";

    public static final String B_DISTRIBUTOR_CHECK_EXIT_ERROR = "B_DISTRIBUTOR_CHECK_EXIT_ERROR";

    public static final String B_DISTRIBUTOR_CHECK_USER_ERROR = "B_DISTRIBUTOR_CHECK_USER_ERROR";

    public static final String B_DISTRIBUTOR_CHECK_NULL = "B_DISTRIBUTOR_CHECK_NULL";

    public static final String B_DISTRIBUTOR_CHECK_FINISH = "B_DISTRIBUTOR_CHECK_FINISH";

    public static final String B_DISTRIBUTOR_CHECK_FIELD_ERROR = "B_DISTRIBUTOR_CHECK_FIELD_ERROR";
    public static final String P_DISTRIBUTOR_ADDRESS_NULL = "P_DISTRIBUTOR_ADDRESS_NULL";
    public static final String P_DISTRIBUTOR_FINANCIAL_NULL = "P_DISTRIBUTOR_FINANCIAL_NULL";
    public static final String P_DISTRIBUTOR_ONE_DEFAULT_SCALE_PRICE_NULL =
        "P_DISTRIBUTOR_ONE_DEFAULT_SCALE_PRICE_NULL";
    public static final String P_DISTRIBUTOR_ERP_BALANCE_FLAG = "P_DISTRIBUTOR_ERP_BALANCE_FLAG";
    public static final String P_DISTRIBUTOR_APPLY_STATUS_1_ERROR = "P_DISTRIBUTOR_APPLY_STATUS_1_ERROR";

    public static final String B_DISTRIBUTOR_WX_OPEN_ID_NULL = "B_DISTRIBUTOR_WX_OPEN_ID_NULL";
    /**
     * 分销商是否分账不能为空（设置开启了C端模式、且自己收款的情况下必填）
     */
    public static final String B_DISTRIBUTOR_SUB_ACCOUNT_FLAG_NULL="B_DISTRIBUTOR_SUB_ACCOUNT_FLAG_NULL";

    /**
     * 开启了分账模式、必须要设置分账配置
     */
    public static final String P_DISTRIBUTOR_SUB_ACCOUNT_CONFIG_NULL = "P_DISTRIBUTOR_SUB_ACCOUNT_CONFIG_NULL";

    public static final String B_DISTRIBUTOR_EXTEND_DATA_IS_NOT_EXISTS = "B_DISTRIBUTOR_EXTEND_DATA_IS_NOT_EXISTS";
}
