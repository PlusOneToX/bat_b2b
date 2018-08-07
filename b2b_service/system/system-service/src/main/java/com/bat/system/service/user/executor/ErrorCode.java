package com.bat.system.service.user.executor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
public class ErrorCode {
    /**
     * 用户创建异常
     */
    public static final String B_USER_CREATE_EXCEPTION = "B_USER_CREATE_EXCEPTION";
    /**
     * 用户角色创建异常
     */
    public static final String B_USER_ROLE_CREATE_EXCEPTION = "B_USER_ROLE_CREATE_EXCEPTION";
    /**
     * 用户品牌创建异常
     */
    public static final String B_USER_BRAND_CREATE_EXCEPTION = "B_USER_BRAND_CREATE_EXCEPTION";
    /**
     * 用户品牌更新异常
     */
    public static final String B_USER_UPDATE_EXCEPTION = "B_USER_UPDATE_EXCEPTION";
    public static final String B_USER_ID_NOT_EXISTS = "B_USER_ID_NOT_EXISTS";
    public static final String B_ERP_USER_NO_EXISTS = "B_ERP_USER_NO_EXISTS";
    public static final String B_USER_NAME_EXISTS = "B_USER_NAME_EXISTS";
    public static final String B_USER_NOT_EXISTS = "B_USER_NOT_EXISTS";
    public static final String B_USER_STATUS_DISABLE = "B_USER_STATUS_DISABLE";
    public static final String B_USER_NAME_OR_PASSWORD_ERROR = "B_USER_NAME_OR_PASSWORD_ERROR";
    public static final String B_RPC_LOGIN_ERROR = "B_RPC_LOGIN_ERROR";
    public static final String B_PASSWORD_ERROR = "B_PASSWORD_ERROR";
    public static final String B_OLD_PASSWORD_ERROR = "B_OLD_PASSWORD_ERROR";
    /**
     * 角色创建异常
     */
    public static final String B_ROLE_CREATE_EXCEPTION = "B_ROLE_CREATE_EXCEPTION";
    /**
     * 角色权限创建异常
     */
    public static final String B_ROLE_PERMISSION_CREATE_EXCEPTION = "B_ROLE_PERMISSION_CREATE_EXCEPTION";
    /**
     * 角色更新异常
     */
    public static final String B_ROLE_UPDATE_EXCEPTION = "B_ROLE_UPDATE_EXCEPTION";
    public static final String B_ROLE_ID_NOT_EXISTS = "B_ROLE_ID_NOT_EXISTS";
    /**
     * 组织ID 不能为空
     */
    public static final String B_ORGANIZATION_ID_NULL = "B_ORGANIZATION_ID_NULL";
    /**
     * ERP 组织代码已存在
     */
    public static final String B_ERP_DEPARTMENT_ID_EXISTS = "B_ERP_DEPARTMENT_ID_EXISTS";
    public static final String B_PERMISSION_ID_NOT_EXISTS = "B_PERMISSION_ID_NOT_EXISTS";
    public static final String B_MENU_PERMISSION_EXISTS = "B_MENU_PERMISSION_EXISTS";
    public static final String B_MENU_ID_NOT_EXISTS = "B_MENU_ID_NOT_EXISTS";
    public static final String B_ERROR = "B_ERROR";
    public static final String B_PAGE_SIZE_TOO_LARGE = "B_PAGE_SIZE_TOO_LARGE";
}
