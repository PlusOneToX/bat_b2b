package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/20 11:34
 */
public enum DeliverStatus {
    Undelivered("未发货", 1), Delivering("出库中", 2), PartDelivered("部分发货", 3), Delivered("已发货", 4);

    private String name;
    private Integer value;

    DeliverStatus(String name, int value) {
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
