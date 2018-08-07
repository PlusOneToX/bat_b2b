package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 8:47
 */
@Data
@ApiModel(description = "C端客户微信小程序授权登录")
public class CustomerWxOfficialProgramLoginCmd {
    @NotBlank(message = "P_DISTRIBUTOR_WX_CODE_NULL")
    @ApiModelProperty(value = "微信code", required = true, example = "bat")
    private String code;
    @NotBlank(message = "P_DISTRIBUTOR_WX_APP_ID_NULL")
    @ApiModelProperty(value = "微信公众号appid", required = true, example = "bat")
    private String appId;
}
