package com.bat.dubboapi.financial.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BaseResponse implements Serializable {
    public final static String INVALID_TOKEN = "te";
    public final static String ERP_UNDEFINED_ERROR_CODE = "E1"; // 没有定义信用档案
    public final static String ERP_CHECK_ERROR_CODE = "E2";// 信用检查不通过
    public final static String ERP_CHECK_LAPSE_CODE = "E3";// 信用档案已失效
    @JsonProperty("Code")
    protected String code;
    @JsonProperty("Message")
    protected String message;

    public boolean isSuccess() {
        return getCode().equals("0");
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "Code='" + code + '\'' + ", Message='" + message + '\'' + '}';
    }
}
