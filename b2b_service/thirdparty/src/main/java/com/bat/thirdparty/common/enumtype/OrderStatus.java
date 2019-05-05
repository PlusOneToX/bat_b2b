package com.bat.thirdparty.common.enumtype;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/20 11:34
 */
public enum OrderStatus {
    PENDING("待确认", 1), CONFIRMED("已确认", 2), REDUSE("已拒绝", 3), CANCELLED("已取消", 4), COMPLETED("已完成", 5);

    private String name;
    private Integer value;

    OrderStatus(String name, int value) {
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
