package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserOpenIdQry {

    @ApiModelProperty(value = "openId")
    @NotBlank(message = "D_COMMON_OPENID_NULL")
    private String openId;


}
