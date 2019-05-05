package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValidateSystemResponse extends BaseResponse {
    @JsonProperty("AccessToken")
    private String accessToken;
}
