package com.bat.financial.common.constant.subaccount;

public enum SubAccountTypeEnum {

    MERCHANT_ID("商户号"),

    PERSONAL_OPENID("由父商户APPID转换得到"),

    PERSONAL_SUB_OPENID("个人sub_openid（由子商户APPID转换得到）");

    private String type;

    SubAccountTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
