package com.bat.order.service.common.enumtype;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/20 11:34
 */
public enum OrderStockType {
    IN("在库订单", 1), ON_WAY("在途订单", 2), MTO("预售订单", 3), IN_ON_WAY("在库在途订单", 4), OUTSOURCING("委外", 5);

    private String name;
    private Integer value;

    OrderStockType(String name, int value) {
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
