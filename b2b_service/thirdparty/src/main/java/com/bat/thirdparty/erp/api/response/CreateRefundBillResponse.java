package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRefundBillResponse extends BaseResponse {
    @JsonProperty("Data")
    private String Data; // erp的退款单据编号

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "CreateRefundBillResponse{" + "Data='" + Data + '\'' + ", code='" + code + '\'' + ", message='" + message
            + '\'' + '}';
    }
}
