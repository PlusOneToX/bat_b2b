package com.bat.financial.api.base;

public class ErrorCode {
    // 参数为空的错误情况
    public final static String P_NOTNULL = "P_NOTNULL";
    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";

    public static final String B_AOP_EXCEPTION = "B_AOP_EXCEPTION";
    public static final String B_ACCOUNT_AMOUNT_EXCEPTION = "B_ACCOUNT_AMOUNT_EXCEPTION";
    public static final String B_ACCOUNT_CHANGE_TYPE_EXCEPTION = "B_ACCOUNT_CHANGE_TYPE_EXCEPTION";
    public static final String B_ACCOUNT_CHANGE_TYPE_NULL = "B_ACCOUNT_CHANGE_TYPE_NULL";
}
