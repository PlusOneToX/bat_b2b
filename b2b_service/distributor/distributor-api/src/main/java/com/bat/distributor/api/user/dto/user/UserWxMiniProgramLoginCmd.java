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
@ApiModel(description = "分销客户微信小程序授权登录")
public class UserWxMiniProgramLoginCmd {
    @NotBlank(message = "P_DISTRIBUTOR_WX_APP_ID_NULL")
    @ApiModelProperty(value = "微信小程序appid", required = true, example = "bat")
    private String appId;
    @NotBlank(message = "P_DISTRIBUTOR_WX_CODE_NULL")
    @ApiModelProperty(value = "微信code", required = true, example = "bat")
    private String code;
    @NotBlank(message = "P_DISTRIBUTOR_WX_DATA_NULL")
    @ApiModelProperty(value = "加密数据", required = true, example = "bat")
    private String encryptedData;
    @NotBlank(message = "P_DISTRIBUTOR_WX_IV_NULL")
    @ApiModelProperty(value = "微信iv", required = true, example = "bat")
    private String iv;
    @ApiModelProperty(value = "头像", required = false, example = "bat")
    private String avatarUrl;
}
