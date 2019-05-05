package com.bat.thirdparty.common;

public class ThirdCommonConstant {

    /**
     * 远程调用状态 1、成功 0、失败
     */
    public static final Short API_REQUEST_SUCCESS = 1;

    public static final Short API_REQUEST_FAIL = 0;
    /**
     * 自动下推出库 1.是 0.否auto_delivery
     */
    public static final Short AUTO_DELIVERY_0 = 0;
    public static final Short AUTO_DELIVERY_1 = 1;

    /**
     * 结算方式类型，1为立即支付，2为期间结算
     */
    public static final Short PAY_WAY_1 = 1;
    public static final Short PAY_WAY_2 = 2;

    /**
     * 货币类型 1-人民币 2-美元
     */
    public static final Short COIN_TYPE_1 = 1;
    public static final Short COIN_TYPE_2 = 2;

    /**
     * 税种类型 1-一般纳税人 2-小规模纳税人 3-个人
     */
    public static final Short TAX_TYPE_1 = 1;
    public static final Short TAX_TYPE_2 = 2;
    public static final Short TAX_TYPE_3 = 3;
    /**
     * 1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭)
     */
    public static final Short ORDER_ERP_STATUS_1 = 1;
    public static final Short ORDER_ERP_STATUS_2 = 2;
    public static final Short ORDER_ERP_STATUS_5 = 5;

    /**
     * 共有的删除标记、1、是删除 0、否
     */
    public static final Short COMMON_DEL_FLAG_YES = 1;

    public static final Short COMMON_DEL_FLAG_NO = 0;

    /**
     * 启用状态 1、是 0、否
     */
    public static final Short COMMON_OPEN_FLAG_YES = 1;

    public static final Short COMMON_OPEN_FLAG_NO = 0;

    /**
     * 9-第三方接口服务 10-MongoDB
     */
    public static final Short MODEL_TYPE9 = 9;
    public static final Short MODEL_TYPE10 = 10;

    /**
     * 特殊类型, 1 普通,2 mto,3 现款,4 定制,5 直运
     */
    public final static Short SPECIAL_FLAG_1 = 1;
    public final static Short SPECIAL_FLAG_2 = 2;
    public final static Short SPECIAL_FLAG_3 = 3;
    public final static Short SPECIAL_FLAG_4 = 4;
    public final static Short SPECIAL_FLAG_5 = 5;
    /**
     * 平台租户文件存储类型 1 阿里云
     */
    public static final Short OSS_TYPE_ALI = 1;

    /**
     * 平台租户url类型 1-后台 2-前台pc端 3-前台h5端4-店铺二维码 5-分销商申请二维码 6-后端接口baseurl
     */
    public static final Short URL_TYPE_DISTRIBUTOR_QRCODE = 5;

    /**
     * 平台租户短信类型 1 阿里云
     */
    public static final Short SMS_TYPE_ALI = 1;

    /**
     * 平台租户ERP配置类型 1 金蝶
     */
    public static final Short ERP_TYPE_JD = 1;

    /**
     * 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb
     */
    public static final Short MONGODB_TYPE = 10;

    /**
     * 是与否 1、是 0、否
     */
    public static final short COMMON_FLAG_YES = 1;

    public static final short COMMON_FLAG_NO = 0;

    /**
     * url类型：1-后台 2-前台pc端 3-前台h5端4-店铺二维码 5-分销商申请二维码 6-后端接口baseurl
     */
    public static final Short URL_TYPE_4 = 4;
}
