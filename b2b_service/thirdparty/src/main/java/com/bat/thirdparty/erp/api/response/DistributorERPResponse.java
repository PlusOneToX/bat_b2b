package com.bat.thirdparty.erp.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.thirdparty.erp.api.response.dto.DistributorERPEntity;
import com.bat.thirdparty.erp.api.response.dto.DistributorERPError;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DistributorERPResponse {

    @JsonProperty("Result")
    private DistributorResponseResult Result;

    @Data
    @ToString
    public static class DistributorResponseResult {
        @JsonProperty("ResponseStatus")
        private DistributorResponseStatus ResponseStatus;
    }

    @Data
    @ToString
    public static class DistributorResponseStatus {
        @JsonProperty("IsSuccess")
        private boolean IsSuccess;
        @JsonProperty("SuccessEntitys")
        private List<DistributorERPEntity> SuccessEntitys;
        @JsonProperty("Errors")
        private List<DistributorERPError> Errors;
    }
}
