package com.bat.flexible.manager.common.constant.index;

/**
 * 首页分销商推荐异常枚举类
 */
public enum DistributorBannerErrorEnum {


    IdNullError(10501,"id不能为空"),

    IdError(10502,"id错误"),

    EndTimeLessThenNow(10503,"结束时间不能小于现在"),

    EndTimeMustBigThenStartTime(10504,"结束时间必须要大于开始时间"),

    DistributorIdNullError(10505,"分销商id不能为空"),

    SeriesIdNullError(10506,"主题系列id不能为空"),

    SeriesPictureListNullError(10507,"主题系列图片列表不能为空"),

    ExternalLinkNullError(10508,"活动链接不能为空"),
    ;

    private Integer code;

    private String msg;


    DistributorBannerErrorEnum(Integer code, String msg) {
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
