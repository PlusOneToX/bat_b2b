package com.bat.thirdparty.common.log;

/**
 * 订单日志记录常量类
 */
public class OrderBusinessLogConstant {

    //取消订单 操作来源 1、第三方分销商 2、工厂 3、ERP
    public static final Short ORDER_CANCEL_SOURCE_DISTRIBUTOR =1;

    public static final Short ORDER_CANCEL_SOURCE_FACTORY =2;

    public static final Short ORDER_CANCEL_SOURCE_ERP =3;

    //搜索类型： 1、单据编号 2、调用平台名称 3、请求内容 4、返回结果（响应）
    public static final Short LOG_PAGE_SEARCH_TYPE_BUSINESS_NO =1;

    public static final Short LOG_PAGE_SEARCH_TYPE_PLATFORM_NAME =2;

    public static final Short LOG_PAGE_SEARCH_TYPE_REQUEST_PARAMS =3;

    public static final Short LOG_PAGE_SEARCH_TYPE_RESPONSE =4;


}
