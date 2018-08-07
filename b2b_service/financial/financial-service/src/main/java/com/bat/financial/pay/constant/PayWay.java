package com.bat.financial.pay.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 15:26
 */
public class PayWay {
    /**
     * 支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
     */
    public static final short ALIPAY = 1;
    public static final short WXPAY = 2;
    public static final short INTERVAL = 3;
    public static final short OFFLINE = 4;
    public static final short BALANCE = 5;
    public static final short KUAIQIAN = 6;
    public static final short BALANCE_ALIPAY = 7;
    public static final short BALANCE_WX = 8;
    public static final short BALANCE_KUAIQIAN = 9;
}
