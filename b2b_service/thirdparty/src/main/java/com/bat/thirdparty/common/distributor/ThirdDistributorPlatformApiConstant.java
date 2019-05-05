package com.bat.thirdparty.common.distributor;

/**
 * 分销商平台常量类
 */
public class ThirdDistributorPlatformApiConstant {

    //sys_platform_api 事务类型: 事务类型: 1为物流信息推送，2为订单信息推送，3 定制信息推送，4 获取定制价格  5、订单编号回传（手动触发失败日志、回传单号回第三方 若后期有扩展，会进行追加
    public static final Short Distributor_SYS_PLATFORM_API_TYPE_LOGISTICS_PUSH =1;

    public static final Short Distributor_SYS_PLATFORM_API_TYPE_ORDER_PUSH =2;

    public static final Short Distributor_SYS_PLATFORM_API_TYPE_DIY_DETAIL_PUSH =3;

    public static final Short Distributor_SYS_PLATFORM_API_TYPE_PRICE_QUERY =4;

    public static final Short Distributor_SYS_PLATFORM_API_TYPE_ORDER_NO_CALLBACK =5;

    public static final Short Distributor_SYS_PLATFORM_API_TYPE_ORDER_CANCEL =6;

    /**
     * 物流信息推送类型时不能为空 1、类似九级之类的推送（推送一大堆数据） 2、模式（只推送订单号和快递单号）
     */
    public static final Short LogisticsSendTypeMore=1;

    /**
     * 物流信息推送类型时不能为空 1、类似九级之类的推送（推送一大堆数据） 2、模式（只推送订单号和快递单号）
     */
    public static final Short LogisticsSendTypeLess=2;


    /**
     * 开发源头类型
     */
    public static final Short DevelopSourceJiuJi=1;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceZhaoLianJi=2;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceJiuQuanQiuTx=3;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceSanXing=4;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceJiKe=5;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceWuYin=6;


    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceBaiLi=9;


    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceJinChengCP=8;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceHywh=10;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceHwVmall =13;

    /**
     * 开发源头类型 
     */
    public static final Short DevelopSourceSzAdp=14;

    //订单号回调通知第三方类型
    public static final Short ORDER_NO_CALL_BACK_TYPE_DISTRIBUTOR_EXTERNAL =1;

    public static final Short ORDER_NO_CALL_BACK_TYPE_TAOBAO =2;

}
