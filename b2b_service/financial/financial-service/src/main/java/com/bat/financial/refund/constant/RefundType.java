package com.bat.financial.refund.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/24 11:42
 */
public class RefundType {
    /**
     * 退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式 (如果不传默认不处理退款申请单，人工处理) 4 混合退款方式
     */
    public static final short REFUND_TO_PAYMENT_ACCOUNT = 1;
    public static final short REFUND_TO_BALANCE_ACCOUNT = 2;
    public static final short REFUND_TO_OTHER_WAY = 3;
    public static final short REFUND_TO_COMBINE_WAY = 4;
}
