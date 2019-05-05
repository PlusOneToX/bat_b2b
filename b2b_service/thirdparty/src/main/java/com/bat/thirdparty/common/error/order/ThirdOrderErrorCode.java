package com.bat.thirdparty.common.error.order;

public class ThirdOrderErrorCode {

    /**
     * 订单来源不能为空
     */
    public static final String T_ORDER_SOURCE_NULL ="T_ORDER_SOURCE_NULL";

    /**
     * 时间戳不能为空
     */
    public static final String T_ORDER_TIMESTAMP_NULL ="T_ORDER_TIMESTAMP_NULL";

    /**
     * 签名不能为空
     */
    public static final String T_ORDER_SIGN_NULL ="T_ORDER_SIGN_NULL";

    /**
     * 生产图不能为空
     */
    public static final String T_ORDER_GENERATE_IMAGE_NULL ="T_ORDER_GENERATE_IMAGE_NULL" ;

    /**
     * 预览图不能为空
     */
    public static final String T_ORDER_PREVIEW_IMAGE_NULL ="T_ORDER_PREVIEW_IMAGE_NULL" ;

    /**
     * 下单数量不能为空
     */
    public static final String T_ORDER_COUNT_NULL = "T_ORDER_COUNT_NULL" ;

    /**
     * 省不能为空
     */
    public static final String T_ORDER_ADDRESS_PROVINCE_NULL = "T_ORDER_ADDRESS_PROVINCE_NULL" ;

    /**
     * 省份信息错误
     */
    public static final String T_ORDER_ADDRESS_PROVINCE_ERROR = "T_ORDER_ADDRESS_PROVINCE_ERROR" ;

    /**
     * 市不能为空
     */
    public static final String T_ORDER_ADDRESS_CITY_NULL = "T_ORDER_ADDRESS_CITY_NULL" ;

    /**
     * 市信息错误
     */
    public static final String T_ORDER_ADDRESS_CITY_ERROR = "T_ORDER_ADDRESS_CITY_ERROR" ;

    /**
     * 区不能为空
     */
    public static final String T_ORDER_ADDRESS_AREA_NULL = "T_ORDER_ADDRESS_AREA_NULL" ;

    /**
     * 区信息错误
     */
    public static final String T_ORDER_ADDRESS_AREA_ERROR = "T_ORDER_ADDRESS_AREA_ERROR" ;

    /**
     * 详细地址不能为空
     */
    public static final String T_ORDER_ADDRESS_DETAIL_NULL = "T_ORDER_ADDRESS_DETAIL_NULL" ;

    /**
     * 电话号码不能为空
     */
    public static final String T_ORDER_USER_MOBILE_NULL ="T_ORDER_USER_MOBILE_NULL" ;

    /**
     * 用户姓名不能为空
     */
    public static final String T_ORDER_USER_NAME_NULL = "T_ORDER_USER_NAME_NULL";

    /**
     * 用户编码不能为空
     */
    public static final String T_ORDER_USER_NO_NULL ="T_ORDER_USER_NO_NULL" ;

    /**
     * 订单id不能为空
     */
    public static final String T_ORDER_ID_NULL ="T_ORDER_ID_NULL" ;


    /**
     * 订单详情不能为空
     */
    public static final String T_ORDER_DETAIL_NULL = "T_ORDER_DETAIL_NULL";

    /**
     * 该分销商尚未配置配送方式、请与客服沟通处理
     */
    public static final String T_DISTRIBUTOR_LOGISTICS_RELEVANCE_NULL = "T_DISTRIBUTOR_LOGISTICS_RELEVANCE_NULL";

    /**
     * 该订单已同步ERP、请勿重复操作
     */
    public static final String T_ORDER_SYNC_ERP_ALREADY = "T_ORDER_SYNC_ERP_ALREADY";

    /**
     * 该订单不需要同步ERP
     */
    public static final String T_ORDER_NOT_NEED_SYNC_ERP = "T_ORDER_NOT_NEED_SYNC_ERP";

    /**
     * ErpOrderDetailNullMsg   = "订单物料详情必填";
     * ERP同步出库单、订单物料详情列表不能为空
     */
    public final static Integer ErpOrderDetailNullCode = 1204;

    /**
     * ERP单号
     */
    public final static Integer ErpOrderNoNullCode = 1201;

    /**
     * 订单编号不能为空
     */
    public static final String COMMON_ORDER_NO_NULL = "COMMON_ORDER_NO_NULL";

    /**
     * 物料编码必填
     */
    public final static Integer ErpOrderDetailNoNullCode = 1205;


    public final static Integer ErpDeliverOrderNumNoNullCode = 1210;

    /**
     * ERP出库发货数量不能为空
     */
    public final static String T_ORDER_ERP_DELIVERY_NUM_NULL = "T_ORDER_ERP_DELIVERY_NUM_NULL";

    /**
     * 图片路径非法
     */
    public final static String T_ORDER_PICTURE_URL_ILLEGAL = "T_ORDER_PICTURE_URL_ILLEGAL";

    /**
     * 读取网络图片URL异常
     */
    public final static String T_ORDER_PICTURE_INTERNET_URL_READ_EXCEPTION = "T_ORDER_PICTURE_INTERNET_URL_READ_EXCEPTION";

    /**
     * FTP上传图片失败
     */
    public final static String T_ORDER_FTP_UPLOAD_FAIL = "T_ORDER_FTP_UPLOAD_FAIL";

    /**
     * FTP上传文件列表不能为空
     */
    public final static String T_ORDER_FTP_UPLOAD_LIST_ = "T_ORDER_FTP_UPLOAD_FAIL";

    /**
     * ERP取消订单失败
     */
    public final static String T_ORDER_CANCEL_TO_ERP_FAIL = "T_ORDER_CANCEL_TO_ERP_FAIL";

    /**
     * ERP关闭订单失败
     */
    public final static String T_ORDER_INVALID_TO_ERP_FAIL = "T_ORDER_INVALID_TO_ERP_FAIL";

    /**
     * 出库单号不能为空
     */
    public static final Integer T_ERP_OUTBOUND_ORDER_NO_NULL_CODE=200011;

    /**
     * 出库单号不能为空
     */
    public static final String T_ERP_OUTBOUND_ORDER_NO_NULL_MSG="T_ERP_OUTBOUND_ORDER_NO_NULL_MSG";

    /**
     * ERP操作状态不能为空
     */
    public static final Integer T_ERP_OPERATE_STATUS_NULL_CODE=200012;

    /**
     * ERP操作状态不能为空
     */
    public static final String T_ERP_OPERATE_STATUS_NULL_MSG="T_ERP_OPERATE_STATUS_NULL_MSG";

    /**
     * ERP同步发货单到B2B异常
     */
    public static final String T_ERP_SYNC_OUTBOUND_B2B_ERROR_MSG="T_ERP_SYNC_OUTBOUND_B2B_ERROR_MSG";

    /**
     * ERP同步发货单到B2B异常编码
     */
    public static final Integer T_ERP_SYNC_OUTBOUND_B2B_ERROR_CODE =20013;

    /**
     * ERP同步发货单状态变更到B2B异常
     */
    public static final String T_ERP_SYNC_OUTBOUND_STATUS_B2B_ERROR_MSG="T_ERP_SYNC_OUTBOUND_STATUS_B2B_ERROR_MSG";

    /**
     * ERP同步发货单状态变更到B2B异常编码
     */
    public static final Integer T_ERP_SYNC_OUTBOUND_STATUS_B2B_ERROR_CODE =20014;

    /**
     * SKU编码不能为空
     */
    public static final String T_SKU_NO_NULL="T_SKU_NO_NULL";

    /**
     * erp单号为空；无法同步
     */
    public static final String T_ERP_NO_NULL_CANT_SYN="T_ERP_NO_NULL_CANT_SYN";


    /**
     * 订单已经同步工厂；无法取消
     */
    public static final String HAS_SYN_FACTORY_CANT_CANCEL="HAS_SYN_FACTORY_CANT_CANCEL";


}
