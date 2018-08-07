package com.bat.financial.pay.constant;

/**
 * @author: lim
 * @description: 交易状态 在微信状态的基础上 涵盖支付宝的状态
 * @date: 2018/7/27 17:09
 */
public class TradeStatus {
    // 微信
    // 交易状态，枚举值：
    // SUCCESS：支付成功
    // REFUND：转入退款
    // NOTPAY：未支付
    // CLOSED：已关闭
    // REVOKED：已撤销（付款码支付）
    // USERPAYING：用户支付中（付款码支付）
    // PAYERROR：支付失败(其他原因，如银行返回失败)
    // ACCEPT：已接收，等待扣款

    // 支付宝交易状态：
    // WAIT_BUYER_PAY（交易创建，等待买家付款）、
    // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
    // TRADE_SUCCESS（交易支付成功）、
    // TRADE_FINISHED（交易结束，不可退款）

    /**
     * 不参与自定义返回
     */
    public static class AliPayTradeState {
        public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        public static final String TRADE_CLOSED = "TRADE_CLOSED";
        public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
        public static final String TRADE_FINISHED = "TRADE_FINISHED";
    }

    // 以微信状态为主 包含支付宝状态（TRADE_FINISHED/暂时没有对应状态）
    // 自定义返回状态
    public static final String SUCCESS = "SUCCESS";
    public static final String REFUND = "REFUND";
    public static final String NOTPAY = "NOTPAY";
    public static final String CLOSED = "CLOSED";
    public static final String REVOKED = "REVOKED";
    public static final String USERPAYING = "USERPAYING";
    public static final String PAYERROR = "PAYERROR";
    public static final String ACCEPT = "ACCEPT";
    // 未知错误
    public static final String ERROR = "ERROR";

}
