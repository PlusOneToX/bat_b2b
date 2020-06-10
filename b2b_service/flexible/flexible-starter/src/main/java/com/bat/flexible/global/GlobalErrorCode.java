package com.bat.flexible.global;

public class GlobalErrorCode {
    // 参数为空的错误情况
    public final static String PARAM_VALID_ERROR = "PARAM_VALID_ERROR";

    // 主键id的错误情况
    public final static String ID_ERROR = "ID_ERROR";

    // 系统错误、捕获不到有信息异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";

    // 处理缓存异常、捕获不到有信息异常情况
    public final static String JETCACHE_EXCEPTION = "JETCACHE_EXCEPTION";

}
