package com.bat.thirdparty.common;

public class CommonErrorCode {
    // 参数为空的错误情况
    public final static String P_NOTNULL = "P_NOTNULL";
    // 系统异常情况
    public final static String SYSTEM_EXCEPTION = "SYSTEM_EXCEPTION";
    // 租户编码为空tenantNo
    public final static String TENANT_NO_NULL = "TENANT_NO_NULL";
    // 数据库连接出错
    public final static String SYSTEM_DATA_ERROR = "SYSTEM_DATA_ERROR";

    public final static String B_THIRD_PARTY_FIELD_ERROR = "B_THIRD_PARTY_FIELD_ERROR";
    // 数据库未配置
    public final static String TENANT_DB_NULL = "TENANT_DB_NULL";

    /**
     * 订单物流已经成功同步、不需要再次同步
     */
    public static final String ORDER_DELIVER_SYNC_REPEAT="ORDER_DELIVER_SYNC_REPEAT";

    /**
     * 该订单已生成工厂单号、请勿重复生成
     */
    public static final String ORDER_OTHER_FACTORYNO_EXIST = "ORDER_OTHER_FACTORYNO_EXIST";

}
