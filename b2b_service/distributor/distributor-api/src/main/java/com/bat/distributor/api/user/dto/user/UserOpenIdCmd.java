package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "分销商更新openId")
public class UserOpenIdCmd {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id")
    private Integer id;

    @NotBlank(message = "D_COMMON_OPENID_NULL")
    @ApiModelProperty(value = "openId", required = true, example = "1")
    private String openId;

}
