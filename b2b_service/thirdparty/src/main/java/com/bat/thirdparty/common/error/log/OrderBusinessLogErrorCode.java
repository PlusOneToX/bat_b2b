package com.bat.thirdparty.common.error.log;


public class OrderBusinessLogErrorCode {

    /**
     * 请求体不能为空
     */
    public static String T_LOG_REQUEST_BODY_NULL = "T_LOG_REQUEST_BODY_NULL";

    /**
     * 请求头不能为空
     */
    public static String T_LOG_REQUEST_HEADER_NULL = "T_LOG_REQUEST_HEADER_NULL";

    /**
     * 重试请求失败
     */
    public static String T_LOG_REQUEST_AGAIN_FAIL = "T_LOG_REQUEST_AGAIN_FAIL";

    /**
     * 非第三方订单推送到B2B类型、不能修改
     */
    public static String T_LOG_UPDATE_FAIL_NOT_BELONG_RECEIVE_ORDER = "T_LOG_UPDATE_FAIL_NOT_BELONG_RECEIVE_ORDER";

    /**
     * 调用成功日志不允许删除
     */
    public static String T_LOG_DELETE_FAIL_BY_API_STATUS_SUCCESS = "T_LOG_DELETE_FAIL_BY_API_STATUS_SUCCESS";


}
