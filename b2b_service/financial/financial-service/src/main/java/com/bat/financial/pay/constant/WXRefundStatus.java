package com.bat.financial.pay.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/27 15:44
 */
public class WXRefundStatus {
    /**
     * 退款状态，枚举值：
     * 
     * SUCCESS：退款成功
     * 
     * CLOSE：退款关闭
     * 
     * ABNORMAL：退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往【商户平台—>交易中心】，手动处理此笔退款
     */
    public static final String SUCCESS = "SUCCESS";
    public static final String CLOSE = "CLOSE";
    public static final String ABNORMAL = "ABNORMAL";
}
