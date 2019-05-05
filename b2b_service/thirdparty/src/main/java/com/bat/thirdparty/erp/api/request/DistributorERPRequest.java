package com.bat.thirdparty.erp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.thirdparty.erp.api.request.dto.DistributorERPEntry;

import lombok.Data;

/**
 * Created by apple on 2019/7/1.
 */
@Data
public class DistributorERPRequest {

    @JsonProperty("Model")
    private DistributorERPEntry Model;

}
