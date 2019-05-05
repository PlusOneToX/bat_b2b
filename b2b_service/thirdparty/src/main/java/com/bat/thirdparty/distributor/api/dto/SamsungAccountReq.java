package com.bat.thirdparty.distributor.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "账号id")
public class SamsungAccountReq {
    @ApiModelProperty(value = "账号id", required = true, example = "732b1ddc8838185bba70521c5e492f67a5399cf4ffb698b04d0b88437af5c30e")
    private String accountId;
    private String imei = "imei";
    private String modelName = "modelName";
}
