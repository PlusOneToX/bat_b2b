package com.bat.promotion.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/20 11:34
 */
public enum ReceiveStatus {
    ALL("全部", 0), UN_RECEIVE("待领取", 1), RECEIVED("已领取", 2), UN_USED("未使用", 3), USED("已使用", 4), EXPIRED("已过期", 5);

    private String name;
    private Integer value;

    ReceiveStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
