package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 8:47
 */
@Data
@ApiModel(description = "分销客户微信小程序授权Openid")
public class UserWxMiniProgramOpenIdCmd {
    @NotBlank(message = "P_DISTRIBUTOR_WX_APP_ID_NULL")
    @ApiModelProperty(value = "微信小程序appid", required = true, example = "bat")
    private String appId;
    @NotBlank(message = "P_DISTRIBUTOR_WX_CODE_NULL")
    @ApiModelProperty(value = "微信code", required = true, example = "bat")
    private String code;
}
