package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/10 14:18
 */
public enum OrderTimer {

    // messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    Timer1("14", 10), Timer2("15", 20), Timer3("16", 30), Timer4("17", 60), Timer5("18", 120);

    private String level;
    private Integer value;

    OrderTimer(String level, int value) {
        this.level = level;
        this.value = value;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
