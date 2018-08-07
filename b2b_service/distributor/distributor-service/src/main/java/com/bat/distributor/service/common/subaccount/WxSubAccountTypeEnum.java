package com.bat.distributor.service.common.subaccount;

public enum WxSubAccountTypeEnum {

    MERCHANT_ID("商户ID"),

    PERSONAL_OPENID("个人openid（由父商户APPID转换得到"),

    PERSONAL_SUB_OPENID("个人sub_openid（由子商户APPID转换得到）"),
    ;


    private String name;

    WxSubAccountTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
