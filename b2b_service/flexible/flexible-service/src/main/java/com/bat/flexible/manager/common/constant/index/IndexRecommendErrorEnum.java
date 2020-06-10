package com.bat.flexible.manager.common.constant.index;

/**
 * 首页分销商推荐异常枚举类
 */
public enum IndexRecommendErrorEnum {

    DistributorIndexRecommendMoreThenOneError(10401,"首页推荐分销商只允许设置一个"),

    IdNullError(10402,"id不能为空"),

    IdError(10403,"id错误"),

    DistributorIdNullError(10404,"分销商id不能为空"),

    ;

    private Integer code;

    private String msg;


    IndexRecommendErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
