package com.bat.promotion.api.base;

public class ErrorCode {
    // 参数为空的错误情况
    public final static String P_NOTNULL = "P_NOTNULL";
    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";

    public static final String B_AOP_EXCEPTION = "B_AOP_EXCEPTION";
    /**
     * 操作失败
     */
    public static final String COMMON_OPERATE_FAIL = "COMMON_OPERATE_FAIL";

    /**
     * 第几行
     */
    public static final String ROW_NUM = "ROW_NUM";
}
