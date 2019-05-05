package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SysPUSHOUTSTOCKResponse extends BaseResponse {

    @JsonProperty("Data")
    private String Data; //采购单编号

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
