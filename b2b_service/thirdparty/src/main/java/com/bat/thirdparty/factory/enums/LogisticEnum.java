package com.bat.thirdparty.factory.enums;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/30 11:47
 */
public enum LogisticEnum {
    HTKY("汇通"),YTO("圆通"),YUNDA("韵达"),ZTO("中通"),STO("申通"),SF("顺丰"),POSTB("邮政");

    private String logisticName;

    LogisticEnum(String LogisticName) {
        this.logisticName = logisticName;
    }

    public String getLogisticName() {
        return logisticName;
    }

}
