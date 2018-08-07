package com.bat.distributor.service.common.subaccount;

public class SubAccountErrorCode {

    /**
     * 分账实效类型为延迟时、分账时间不能为空
     */
    public static final String D_SUB_ACCOUNT_DELAY_TIME_NULL = "D_SUB_ACCOUNT_DELAY_TIME_NULL";

    /**
     * 分账等级尚未配置分账比例
     */
    public static final String D_SUB_ACCOUNT_LEVEL_NOT_SET_RATIO="D_SUB_ACCOUNT_LEVEL_NOT_SET_RATIO";

    /**
     * 该业务员的手机号码已关联同一个分销商
     */
    public static final String D_SALEMAN_MOBILE_RELEVANCE_DISTRIBUTOR_EXISTS="D_SALEMAN_MOBILE_RELEVANCE_DISTRIBUTOR_EXISTS";

    /**
     * 分销商分账等级列表不能为空
     */
    public static final String D_SUB_ACCOUNT_LEVEL_NAME_LIST_NULL ="D_SUB_ACCOUNT_LEVEL_NAME_LIST_NULL" ;

    /**
     * 分账类型不能为空
     */
    public static final String D_SUB_ACCOUNT_AGING_TYPE_NULL ="D_SUB_ACCOUNT_AGING_TYPE_NULL" ;

    /**
     * 分账业务员电话号码错误
     */
    public static final String D_SUB_ACCOUNT_SALEMAN_MOBILE_ERROR ="D_SUB_ACCOUNT_SALEMAN_MOBILE_ERROR" ;

    /**
     * 分账业务员已被冻结、请与客服沟通处理
     */
    public static final String D_SUB_ACCOUNT_SALEMAN_STATUS_FREEZE = "D_SUB_ACCOUNT_SALEMAN_STATUS_FREEZE";

    /**
     * 业务员身份类型不能为空
     */
    public static final String D_SUB_ACCOUNT_IDENTITY_TYPE_NULL ="D_SUB_ACCOUNT_IDENTITY_TYPE_NULL" ;

    /**
     * 分账等级名称不能为空
     */
    public static final String D_SUB_ACCOUNT_LEVEL_NAME_NULL = "D_SUB_ACCOUNT_LEVEL_NAME_NULL";
    /**
     * 分账等级名称错误
     */
    public static final String D_SUB_ACCOUNT_LEVEL_NAME_ERROR = "D_SUB_ACCOUNT_LEVEL_NAME_ERROR";

    /**
     * 分账等级名称同名超过一个
     */
    public static final String D_SUB_ACCOUNT_LEVEL_NAME_MORE_THEN_ONE = "D_SUB_ACCOUNT_LEVEL_NAME_MORE_THEN_ONE";

    /**
     * 上级业务员名称错误
     */
    public static final String D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_ERROR = "D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_ERROR";

    /**
     * 业务员名称同名超过一个
     */
    public static final String D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_MORE_THEN_ONE = "D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_MORE_THEN_ONE";

    /**
     * 商户号不能为空
     */
    public static final String D_MERCHANT_NUMBER_NULL = "D_MERCHANT_NUMBER_NULL";

    /**
     * 等级分账比例和大于服务商账户分账百分比
     */
    public static final String D_SUB_ACCOUNT_RATIO_GREATER_THEN_PAY ="D_SUB_ACCOUNT_RATIO_GREATER_THEN_PAY" ;
}
