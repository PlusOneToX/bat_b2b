package com.bat.thirdparty.erp.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by apple on 2019/7/9.
 */
@Data
public class DistributorERPEntity {

    @JsonProperty("Id")
    private Integer Id;
    @JsonProperty("Number")
    private String Number;
}
