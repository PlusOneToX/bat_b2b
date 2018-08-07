package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/20 11:34
 */
public enum PayWay {
    Alipay("支付宝", 1), WeChat("微信支付", 2), Period_settlement("区间结算", 3), Offline_payment("线下转账", 4),
    Balance_payment("余额支付", 5), Kuaiqian_payment("快钱支付", 6), Balance_Alipay_payment("余额+支付宝", 7),
    Balance_WeChat_payment("余额+微信支付", 8), Balance_Kuaiqian_payment("余额+快钱支付", 9);

    private String name;
    private Integer value;

    PayWay(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getValue() {
        return Short.valueOf(String.valueOf(value));
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
