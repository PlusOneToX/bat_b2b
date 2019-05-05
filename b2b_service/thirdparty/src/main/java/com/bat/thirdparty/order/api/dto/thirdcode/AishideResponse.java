package com.bat.thirdparty.order.api.dto.thirdcode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 爱施德响应
 */
public class AishideResponse {

    @JsonProperty("Code")
    private Integer Code;

    @JsonProperty("IsSuccess")
    private Boolean IsSuccess;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Data")
    private Object Data;

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
