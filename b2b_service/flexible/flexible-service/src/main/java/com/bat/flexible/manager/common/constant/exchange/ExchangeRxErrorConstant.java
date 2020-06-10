package com.bat.flexible.manager.common.constant.exchange;

/**
 * 兑换卡对柔性异常枚举类
 */
public enum ExchangeRxErrorConstant {

    ItemListNullError(10251,"兑换卡商品列表不能为空"),

    MaterialIdNullError(10252,"材质id不能为空"),

    ModelIdNullError(10253,"型号id不能为空"),

    PictureIdNullError(10254,"图片id不能为空"),

    SecretCodeNullError(10255,"兑换码列表不能为空"),

    SecretCodeError(10256,"暗码错误"),

    ExchangeFailByCodeStatusInit(10257,"兑换码未激活、无法使用"),

    ExchangeFailByCodeStatusUsed(10258,"兑换码已使用、无法重复使用"),

    ExchangeFailByCodeStatusOverdue(10259,"兑换码已过期、无法使用"),

    ExchangeFailByCodeStatusInvalid(10260,"兑换码已作废、无法使用"),

    ExchangeFailByCardStatusNoStarting(10261,"兑换卡活动非进行状态、无法使用"),

    MaterialIdError(10262,"材质id错误"),

    ModelIdError(10263,"型号id错误"),

    PictureIdError(10264,"该图片不属于该兑换码对应活动"),

    ModelRelaNullError(10265,"该兑换卡活动型号关联为空"),

    PictureRelaNullError(10266,"该兑换卡活动图片关联为空"),

    ExchangeCodeNoB2BSaleError(10267,"非法兑换卡"),
    ;


    private Integer code;

    private String msg;

    ExchangeRxErrorConstant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
