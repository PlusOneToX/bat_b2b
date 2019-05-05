package com.bat.thirdparty.distributor.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(description = "账号id")
public class SamsungAccountQry {
    @ApiModelProperty(value = "账号id", required = true, example = "732b1ddc8838185bba70521c5e492f67a5399cf4ffb698b04d0b88437af5c30e")
    private String accountId;
    @ApiModelProperty(value = "唯一id(openid) 可能不会传", required = false, example = "75ff1a0a3be2ebc4290002e9f2acf8e13208449ce6265bf12e1c8e79ae8867bbd189")
    private String authenticateUserID;
    @ApiModelProperty(value = "优惠券编码", required = false, example = "C01A53")
    private String couponCode;
}
