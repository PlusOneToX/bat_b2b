package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/10 14:18
 */
public enum OperateType {

    Order("下单", 1), OrderCheck("订单审核", 2), OrderDelivered("订单发货", 3);

    private String name;
    private Integer value;

    OperateType(String name, int value) {
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
