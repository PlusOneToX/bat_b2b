package com.bat.thirdparty.erp.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

/**
 * Created by apple on 2019/7/9.
 */
@Data
@ToString
public class DistributorERPError {

    @JsonProperty("FieldName")
    private String FieldName;
    @JsonProperty("Message")
    private String Message;
}
