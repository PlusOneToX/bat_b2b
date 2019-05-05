package com.bat.thirdparty.Sumsung.constants;


public class Constant {
	//订单状态
	public final static String unpaid = "unpaid";// 待付款
	public final static String orderProcessing = "orderProcessing";// 待发货
	public final static String paymentDue = "paymentDue";// 待付款
	public final static String paid = "paid";// 已付款
	public final static String orderDelivered = "orderDelivered";// 已收货
	public final static String orderSucceeded = "orderSucceeded";// 已完成
	public final static String orderCancelled = "orderCancelled";// 已取消
	public final static String orderInTransit = "orderInTransit";// 待收货

	// 同步数据返回结果
	public final static String SA_ERROR_CODE_SUCCESS                        = "SA_0000";
	public final static String SA_ERROR_CODE_SERVICE_UNAVAILABLE            = "SA_1000";
	public final static String SA_ERROR_CODE_INTERNAL_SERVER_ERROR          = "SA_1001";
	public final static String SA_ERROR_CODE_PARAM_MISSING                  = "SA_2000";
	public final static String SA_ERROR_CODE_PARAM_VALIDATION               = "SA_2001";
	public final static String SA_ERROR_CODE_PARAM_BINDING                  = "SA_2010";
	public final static String SA_ERROR_CODE_NO_ACCOUNT                     = "SA_4000";
	public final static String SA_ERROR_CODE_ACCOUNT_ALREADY_EXISTS         = "SA_4001";
	public final static String SA_ERROR_CODE_NO_DEVICE_LOGGED_IN            = "SA_4011";
}
