package com.bat.dubboapi.order.order.dto.enmus;

import java.io.Serializable;

public enum FactoryEnum implements Serializable {
    /**
     *
     */
    COMMON("COMMON", "系统模拟发货"), LHW("LHW", "联辉王"), MK("MK", "麦克"), DH("DH", "多鸿"), DH_OLK("DH_OLK", "多鸿欧丽科"),KDS_FK("KDS_FK", "壳大师(飞快)");

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    FactoryEnum(String name, String value) {
        this.name = value;
        this.value = value;
    }

}
