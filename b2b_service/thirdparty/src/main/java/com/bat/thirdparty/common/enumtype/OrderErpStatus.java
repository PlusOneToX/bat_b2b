package com.bat.thirdparty.common.enumtype;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/20 11:34
 */
public enum OrderErpStatus {
    PENDING("待确认", 1), CONFIRMED("已确认", 2), CANCELLED("已取消", 5);

    private String name;
    private Integer value;

    OrderErpStatus(String name, int value) {
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
