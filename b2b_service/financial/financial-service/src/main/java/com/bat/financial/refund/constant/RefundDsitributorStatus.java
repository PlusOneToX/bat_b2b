package com.bat.financial.refund.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/26 21:42
 */
public class RefundDsitributorStatus {
    /**
     * 退款状态: 0未生效（微信退款回调中间状态）,1待确认,2已确认,3已取消
     */
    public static final short INEFFECTIVE = 0;
    public static Short WAIT_CONFIRM = 1;
    public static final short SUCCESS = 2;
    public static final short FAIL = 3;
}
