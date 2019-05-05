package com.bat.thirdparty.distributor.enumtype;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/20 11:34
 */
public enum SamsungResponseStatus {
    SUCCESS("SA_0000", 1);

    private String name;
    private Integer value;

    SamsungResponseStatus(String name, int value) {
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
