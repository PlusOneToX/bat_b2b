package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserPhoneVerifyCodeCmd", description = "分销商前台手机号验证")
public class UserPhoneVerifyCodeCmd extends UserPhoneVerify {
    @NotBlank(message = "P_DISTRIBUTOR_USER_PHONE_CODE_NULL")
    @ApiModelProperty(value = "验证码", required = true, example = "bat")
    private String code;


}
