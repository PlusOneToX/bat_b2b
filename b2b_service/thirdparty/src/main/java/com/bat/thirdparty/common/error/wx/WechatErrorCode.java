package com.bat.thirdparty.common.error.wx;

public class WechatErrorCode {

    /**
     * 生成小程序二维码失败
     */
    public static final String WECHAT_PROGRAM_QR_CODE_CREATE_ERROR="WECHAT_PROGRAM_QR_CODE_CREATE_ERROR";

    /**
     * 微信支付加载私钥异常
     */
    public static final String WECHAT_PAY_PRIVATE_KEY_INIT_ERROR="WECHAT_PAY_PRIVATE_KEY_INIT_ERROR";

    /**
     * 微信支付获取平台证书异常
     */
    public static final String WECHAT_PAY_CERTIFICATE_OF_PLATFORM_QUERY_ERROR="WECHAT_PAY_CERTIFICATE_OF_PLATFORM_QUERY_ERROR";

    /**
     * 微信服务商分账接收人添加异常
     */
    public static final String WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR = "WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR";

    /**
     * 微信服务商分账接收人删除异常
     */
    public static final String WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR = "WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR";

    /**
     * 微信链接获取时签名异常失效返回值
     */
    public static final int WX_SECRET_ERR_CODE_40001 = 40001;
}
