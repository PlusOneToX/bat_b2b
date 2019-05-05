package com.bat.thirdparty.common.error;

/**
 * 第三方服务、通用错误常量类
 */
public class ThirdCommonErrorCode {



    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";

    /**
     * id不能为空
     */
    public static final String COMMON_ID_NULL="COMMON_ID_NULL";

    /**
     * 名称不能为空
     */
    public static final String COMMON_NAME_NULL="COMMON_NAME_NULL";

    /**
     * 列表不能为空
     */
    public static final String COMMON_LIST_NULL="COMMON_LIST_NULL";

    /**
     * 编码不能为空
     */
    public static final String COMMON_CODE_NULL="COMMON_CODE_NULL";

    /**
     * id错误
     */
    public static final String COMMON_ID_ERROR="COMMON_ID_ERROR";

    /**
     * 数量非法
     */
    public static final String T_NUMBER_ILLEGAL = "T_NUMBER_ILLEGAL";

    /**
     * 订单号不能为空
     */
    public static final String COMMON_ORDER_NO_NULL="COMMON_ORDER_NO_NULL" ;

    /**
     * 通用 请勿重复操作
     */
    public static final String COMMON_OPERATE_REPEAT ="COMMON_OPERATE_REPEAT" ;

    /**
     * 操作失败
     */
    public static final String COMMON_OPERATE_FAIL ="COMMON_OPERATE_FAIL" ;

    /**
     * 重新设置请求头参数异常
     */
    public static final String COMMON_SET_HEADER_AGAIN_FAIL ="COMMON_SET_HEADER_AGAIN_FAIL" ;

    /**
     * 通用数据已被删除
     */
    public static final String COMMON_DATA_DEL_ALREADY="COMMON_DATA_DEL_ALREADY";
}
