package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/20 11:34
 */
public enum StockLockType {
    LOCK_IN("在库锁定", 1), LOCK_ON_WAY("在途锁定", 2), LOCK_VMI("vmi锁定", 3);

    private String name;
    private Integer value;

    StockLockType(String name, int value) {
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
