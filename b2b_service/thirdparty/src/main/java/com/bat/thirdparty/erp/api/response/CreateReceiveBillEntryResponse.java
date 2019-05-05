package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateReceiveBillEntryResponse extends BaseResponse {

    @JsonProperty("Data")
    private String Data; // erp的收款单据编号

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "CreateReceiveBillEntryResponse{" + "Data='" + Data + '\'' + ", code='" + code + '\'' + ", message='"
            + message + '\'' + '}';
    }
}
