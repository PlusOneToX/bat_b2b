package com.bat.financial.pay.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 15:33
 */
public class PayStatus {
    /**
     * 订单状态: 0未支付， 1已支付，2已取消
     */
    public static final short WAIT_PAYMENT = 0;
    public static final short COMPLETE_PAYMENT = 1;
    public static final short CANCEL_PAYMENT = 2;

}
