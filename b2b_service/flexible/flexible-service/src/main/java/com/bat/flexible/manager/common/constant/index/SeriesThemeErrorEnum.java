package com.bat.flexible.manager.common.constant.index;

/**
 * 首页分销商推荐异常枚举类
 */
public enum SeriesThemeErrorEnum {


    IdNullError(10451,"id不能为空"),

    IdError(10452,"id错误"),

    DistributorIdNullError(10453,"分销商id不能为空"),

    SeriesIdNullError(10454,"主题系列id不能为空"),

    SeriesDeleteFailByRelaBannerError(10455,"该系列关联了轮播图、请先删除关联该系列的轮播banner"),

    DistributorNoSeriesThemePermissionError(10456,"无关联该主题系列"),
    ;

    private Integer code;

    private String msg;


    SeriesThemeErrorEnum(Integer code, String msg) {
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
