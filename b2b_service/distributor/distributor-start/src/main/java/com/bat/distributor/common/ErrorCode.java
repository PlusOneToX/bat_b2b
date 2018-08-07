package com.bat.distributor.common;

public class ErrorCode {
    // 参数为空的错误情况
    public final static String P_NOTNULL = "P_NOTNULL";
    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";
    // 租户编码为空tenantNo
    public final static String TENANT_NO_NULL = "TENANT_NO_NULL";
    // 数据库连接出错
    public final static String SYSTEM_DATA_ERROR = "SYSTEM_DATA_ERROR";
    // 数据库未配置
    public final static String TENANT_DB_NULL = "TENANT_DB_NULL";

}
