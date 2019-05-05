package com.bat.thirdparty.erp.api.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by apple on 2019/7/1.
 */
@Data
public class DistributorERPCheckRequest {

    @JsonProperty("CreateOrgId")
    private String CreateOrgId;
    @JsonProperty("Numbers")
    private List<String> Numbers;
    @JsonProperty("Ids")
    private String Ids;

}
