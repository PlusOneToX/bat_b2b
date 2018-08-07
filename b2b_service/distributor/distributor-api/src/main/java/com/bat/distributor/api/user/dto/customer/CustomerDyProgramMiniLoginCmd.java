package com.bat.distributor.api.user.dto.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 沙漠
 */
@Data
@ApiModel(description = "C端客户抖音授权登录")
public class CustomerDyProgramMiniLoginCmd {

    @NotBlank(message = "P_DISTRIBUTOR_DY_CODE_NULL")
    @ApiModelProperty(value = "微信code", required = true, example = "bat")
    private String code;
    @NotBlank(message = "P_DISTRIBUTOR_DY_APP_ID_NULL")
    @ApiModelProperty(value = "抖音appid", required = true, example = "bat")
    private String appId;

    @NotBlank(message = "P_DISTRIBUTOR_DY_DATA_NULL")
    @ApiModelProperty(value = "加密数据", required = true, example = "bat")
    private String encryptedData;
    @NotBlank(message = "P_DISTRIBUTOR_DY_IV_NULL")
    @ApiModelProperty(value = "抖音iv", required = true, example = "bat")
    private String iv;
    @ApiModelProperty(value = "头像", required = false, example = "bat")
    private String avatarUrl;
    @ApiModelProperty(value = "国家", required = false, example = "bat")
    private String country;
    @ApiModelProperty(value = "省", required = false, example = "bat")
    private String province;
    @ApiModelProperty(value = "城市", required = false, example = "bat")
    private String city;
    @ApiModelProperty(value = "性别 1、男 2、女", required = false, example = "bat")
    private Short gender;
    @ApiModelProperty(value = "抖音昵称", required = false, example = "bat")
    private String nickName;
}
