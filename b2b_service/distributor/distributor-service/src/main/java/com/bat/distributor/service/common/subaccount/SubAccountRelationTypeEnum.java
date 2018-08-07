package com.bat.distributor.service.common.subaccount;

public enum SubAccountRelationTypeEnum {

    SERVICE_PROVIDER("服务商"),

    STORE("门店"),

    STAFF("员工"),

    STORE_OWNER("店主"),

    PARTNER("合作伙伴"),

    HEADQUARTER("总部"),

    BRAND("品牌方"),

    DISTRIBUTOR("分销商"),

    USER("用户"),

    SUPPLIER("供应商"),

    CUSTOM("自定义"),






    ;


    private String name;

    SubAccountRelationTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
