package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErpSyncResponse {

    @JsonProperty("Data")
    private Object Data;

    @JsonProperty("Code")
    private String Code;

    @JsonProperty("Message")
    private String Message;

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
