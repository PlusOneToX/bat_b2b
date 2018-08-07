package com.bat.financial.common.constant.subaccount;

public enum FrontSubAccountStatus {

    NO_SUB_ACCOUNT((short)0, "不分账"),

    UN_SUB_ACCOUNT((short)1, "待分账"),

    SOME_SUB_ACCOUNT((short)2, "部分分账"),

    ALL_SUB_ACCOUNT((short)3, "全部分账"),

    SUB_ACCOUNT_FAIL((short)4, "分账失败"),

    ;

    private Short id;

    private String name;

    FrontSubAccountStatus(Short id, String name) {
        this.id = id;
        this.name = name;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
