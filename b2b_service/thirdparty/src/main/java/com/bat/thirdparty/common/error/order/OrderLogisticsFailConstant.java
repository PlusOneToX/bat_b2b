package com.bat.thirdparty.common.error.order;

public class OrderLogisticsFailConstant {

    //错误类型 1、查询无响应 2、查询接口报错 3、同步失败 4、订单不给修改（系统订单状态不给修改物流单号）
    public static final Short OrderLogisticsStatusNoResponse = 1;

    //错误类型 1、查询无响应 2、查询接口报错 3、同步失败 4、订单不给修改（系统订单状态不给修改物流单号）
    public static final Short OrderLogisticsStatusQueryException = 2;

    //错误类型 1、查询无响应 2、查询接口报错 3、同步失败 4、订单不给修改（系统订单状态不给修改物流单号）
    public static final Short OrderLogisticsStatusSyncFail = 3;

    //错误类型 1、查询无响应 2、查询接口报错 3、同步失败 4、订单不给修改（系统订单状态不给修改物流单号）
    public static final Short OrderLogisticsStatusSyncReject = 4;
}
