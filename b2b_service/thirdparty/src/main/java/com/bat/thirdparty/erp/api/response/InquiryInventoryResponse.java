package com.bat.thirdparty.erp.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;

import java.util.List;

public class InquiryInventoryResponse extends BaseResponse {

    @JsonProperty("Data")
    private List<InquiryInfo> Data;

    public List<InquiryInfo> getData() {
        return Data;
    }

    public void setData(List<InquiryInfo> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "InquiryInventoryResponse{" +
                "Data=" + Data +
                ", Code='" + code + '\'' +
                ", Message='" + message + '\'' +
                '}';
    }
}
