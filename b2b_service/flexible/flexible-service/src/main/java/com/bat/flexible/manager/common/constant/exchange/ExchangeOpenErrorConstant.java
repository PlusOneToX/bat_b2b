package com.bat.flexible.manager.common.constant.exchange;

/**
 * 兑换卡异常枚举类
 */
public enum ExchangeOpenErrorConstant {

    PlainCodeAndSecretCodeIsNull(10151,"明码和暗码相关参数不能全为空"),

    BoxCodeListNull(10152,"盒码列表不能为空"),

    PlainCodeErrorOrSecretCodeError(10153,"明码或者暗码错误"),

    BoxCodeSendAgainError(10208,"请勿重复回传盒码"),
    ;


    private Integer code;

    private String msg;

    ExchangeOpenErrorConstant(Integer code, String msg) {
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
