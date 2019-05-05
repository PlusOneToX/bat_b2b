package com.bat.thirdparty.erp.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.thirdparty.erp.api.response.dto.BalanceInfo;

public class BalanceInfoResponse extends BaseResponse {
    @JsonProperty("Data")
    private List<BalanceInfo> Data;

    public List<BalanceInfo> getData() {
        return Data;
    }

    public void setData(List<BalanceInfo> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "BalanceInfoResponse{" + "Data=" + Data + ", code='" + code + '\'' + ", message='" + message + '\''
            + '}';
    }
}
