package com.bat.financial.api.pay.constant;

/**
 * @author lx
 */

public enum PayChannel {
    // 支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
    /**
     * 微信，支付宝，快钱
     */
    ALIPAY(1), WXPAY_V2(2), WXPAY_V3(2), BALANCE(5), KUAIQIAN(6),
    /**
     * 支付宝通用
     */
    ALIPAY_COMMON(12),
    /**
     * 支付宝花呗
     */
    ALIPAY_HUABEI(12),
    /**
     * 支付宝当面付
     */
    ALIPAY_FACE_TO_FACE(13),
    /**
     * 支付宝电脑网站支付
     */
    ALIPAY_PAGE(14),
    /**
     * 支付宝手机网站支付
     */
    ALIPAY_WAP(15),
    /**
     * 支付宝手机APP支付
     */
    ALIPAY_APP(16),
    /**
     * 微信jsapi
     */
    WXPAY_JSAPI(21),
    /**
     * 微信app
     */
    WXPAY_APP(22),
    /**
     * 微信H5
     */
    WXPAY_H5(23),
    /**
     * 微信native
     */
    WXPAY_NATIVE(24),
    /**
     * 微信小程序
     */
    WXPAY_MINI_PROGRAM(25),
    /**
     * 微信联合支付
     */
    WXPAY_COMBINE(26),
    /**
     * 快钱人民币网关支付
     */
    KUAIQIAN_MERCHANT(61),
    /**
     * 快钱快捷支付
     */
    KUAIQIAN_QUICK(62),

    /**
     * 微信服务商模式jsapi
     */
    WXPAY_PARTNER_JSAPI(71),

    /**
     * 微信服务商模式NATIVE
     */
    WXPAY_PARTNER_NATIVE(72);
    ;

    private int id;

    PayChannel(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        System.out.println(PayChannel.WXPAY_PARTNER_JSAPI.name());
    }
}
