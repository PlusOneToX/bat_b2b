package com.bat.financial.refund.constant;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/28 15:50
 */
public enum PayStatus {
    UNPAID("未付款", 1), PARTIAL_PAYMENT("部分付款", 2), PAID("已付款", 3), PARTIAL_REFUND("部分退款", 4), APPLY_REFUND("退款申请中", 5),
    REFUND("已退款", 6);

    private String name;
    private Integer value;

    PayStatus(String name, int value) {
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