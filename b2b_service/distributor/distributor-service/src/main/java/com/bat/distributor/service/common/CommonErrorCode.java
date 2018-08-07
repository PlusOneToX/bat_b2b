package com.bat.distributor.service.common;

public class CommonErrorCode {
    public static final String B_DISTRIBUTOR_NULL = "B_DISTRIBUTOR_NULL";
    public static final String B_DISTRIBUTOR_PLATFORM_NULL = "B_DISTRIBUTOR_PLATFORM_NULL";
    public static final String B_DISTRIBUTOR_PLATFORM_NULL_ERROR = "B_DISTRIBUTOR_PLATFORM_NULL_ERROR";
    public static final String B_DISTRIBUTOR_STATUS_ERROR = "B_DISTRIBUTOR_STATUS_ERROR";
    public static final String B_USER_LOGIN_ERROR = "B_USER_LOGIN_ERROR";

    /**
     * 访问商品服务异常
     */
    public static final String C_GOODS_SERVICE_EXCEPTION ="C_GOODS_SERVICE_EXCEPTION";

    /**
     * 访问定制服务异常
     */
    public static final String C_FLEXIBLE_CUSTOM_SERVICE_EXCEPTION ="C_FLEXIBLE_CUSTOM_SERVICE_EXCEPTION";

    /**
     * 数字非法
      */
    public static final String D_COMMON_NUMBER_ILLEGAL ="D_COMMON_NUMBER_ILLEGAL";

    /**
     * 百分比超过100%
     */
    public static final String D_COMMON_NUMBER_PERCENTAGE_GREATER_THEN_HUNDRED ="D_COMMON_NUMBER_PERCENTAGE_GREATER_THEN_HUNDRED";

    /**
     * ID错误
     */
    public static final String D_COMMON_ID_ERROR="D_COMMON_ID_ERROR";

    /**
     * ID不能为空
     */
    public static final String D_COMMON_ID_NULL="D_COMMON_ID_NULL";
    /**
     * 当前用户绑定了多个分销商联系人、请联系客服处理
     */
    public static final String B_DISTRIBUTOR_CONTACTS_USABLE_MORE_THEN_ONE_ERROR = "B_DISTRIBUTOR_CONTACTS_USABLE_MORE_THEN_ONE_ERROR";

    /**
     * 数据已被删除
     */
    public static final String D_COMMON_DATA_DEL_ALREADY="D_COMMON_DATA_DEL_ALREADY";

    /**
     * 请勿重复操做
     */
    public static final String COMMON_OPERATE_REPEAT = "COMMON_OPERATE_REPEAT";

    /**
     * 分销商id不能为空
     */
    public static final String COMMON_DISTRIBUTOR_ID_NULL = "COMMON_DISTRIBUTOR_ID_NULL";

    /**
     * excel解析异常
     */
    public static final String COMMON_EXCEL_PARSING_ERROR = "COMMON_EXCEL_PARSING_ERROR";

    /**
     * 名称不能为空
     */
    public static final String D_COMMON_NAME_NULL = "D_COMMON_NAME_NULL";

    /**
     * 电话号码不能为空
     */
    public static final String D_COMMON_MOBILE_NULL = "D_COMMON_MOBILE_NULL";

    /**
     * EXCEL导入不能为空
     */
    public static final String COMMON_EXCEL_IMPORT_DATA_NULL ="COMMON_EXCEL_IMPORT_DATA_NULL" ;

    /**
     * 手机号码已存在
     */
    public static final String D_COMMON_MOBILE_EXIST ="D_COMMON_MOBILE_EXIST" ;

    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";
}
