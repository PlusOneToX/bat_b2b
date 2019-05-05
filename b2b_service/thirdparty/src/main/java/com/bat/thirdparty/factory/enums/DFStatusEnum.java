package com.bat.thirdparty.factory.enums;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/30 11:47
 */
public enum DFStatusEnum {
    STATUS_0("未处理",0),
    STATUS_1("已传图未提交",1),
    STATUS_2("已提交代发",2),
    STATUS_3("已生产",3),
    STATUS_4("已发货",4),
    STATUS_5("发货失败",5),
    STATUS_6("已反审",6);


    private String name;
    private Integer value;

    DFStatusEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
    public static String getName(Integer value){
        if(STATUS_0.value.equals(value)){
            return STATUS_0.name;
        }else if(STATUS_1.value.equals(value)){
            return STATUS_1.name;
        }else if(STATUS_2.value.equals(value)){
            return STATUS_2.name;
        }else if(STATUS_3.value.equals(value)){
            return STATUS_3.name;
        }else if(STATUS_4.value.equals(value)){
            return STATUS_4.name;
        }else if(STATUS_5.value.equals(value)){
            return STATUS_5.name;
        }else if(STATUS_6.value.equals(value)){
            return STATUS_6.name;
        }else {
            return "未知错误";
        }
    }
}
