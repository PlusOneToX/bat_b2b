package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserPhoneQry", description = "分销商第三方登录手机号")
public class UserPhoneQry {
    @NotBlank(message = "P_DISTRIBUTOR_USER_PHONE_NULL")
    @ApiModelProperty(value = "第三方获取的登录手机号", required = true, example = "bat")
    private String phone;
    @NotBlank(message = "P_DISTRIBUTOR_USER_OPEN_ID_NULL")
    @ApiModelProperty(value = "第三方平台授权唯一标识码", required = true, example = "bat")
    private String openId;
}
