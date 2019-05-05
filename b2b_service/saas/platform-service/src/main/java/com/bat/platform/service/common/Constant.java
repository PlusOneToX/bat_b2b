package com.bat.platform.service.common;

public class Constant {

    /**
     * 数据库源来源：1-自动生成 2-手工填写
     */
    public static final Short SOURCE_TYPE1 = 1;
    public static final Short SOURCE_TYPE2 = 2;

    /**
     * 数据表状态：1-已创建 2-未创建(数据库来源为手工填写默认已创建)
     */
    public static final Short TABLE_FLAG1 = 1;
    public static final Short TABLE_FLAG2 = 2;

    /**
     * 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb 11-redis
     */
    public static final Short MODEL_TYPE1 = 1;
    public static final Short MODEL_TYPE2 = 2;
    public static final Short MODEL_TYPE3 = 3;
    public static final Short MODEL_TYPE4 = 4;
    public static final Short MODEL_TYPE5 = 5;
    public static final Short MODEL_TYPE6 = 6;
    public static final Short MODEL_TYPE7 = 7;
    public static final Short MODEL_TYPE8 = 8;
    public static final Short MODEL_TYPE9 = 9;
    public static final Short MODEL_TYPE10 = 10;
    public static final Short MODEL_TYPE11 = 11;

    /**
     * 状态, 1.启用 0.禁用
     */
    public static final short OPEN_STATUS = 1;
    public static final short CLOSE_STATUS = 0;

    /**
     * 1-后台 2-前台pc端 3-前台h5端4-店铺二维码 5-分销商申请二维码 6-后端接口baseurl
     */
    public static final String TENANT_URL_CACHE_1 = "TENANT_URL_CACHE_1:";
    public static final String TENANT_URL_CACHE_2 = "TENANT_URL_CACHE_2:";
    public static final String TENANT_URL_CACHE_3 = "TENANT_URL_CACHE_3:";
    public static final String TENANT_URL_CACHE_4 = "TENANT_URL_CACHE_4:";
    public static final String TENANT_URL_CACHE_5 = "TENANT_URL_CACHE_5:";
    public static final String TENANT_URL_CACHE_6 = "TENANT_URL_CACHE_6:";

    public static final Short URL_TYPE_1 = 1;
    public static final Short URL_TYPE_2 = 2;
    public static final Short URL_TYPE_3 = 3;
    public static final Short URL_TYPE_4 = 4;
    public static final Short URL_TYPE_5 = 5;
    public static final Short URL_TYPE_6 = 6;
}
