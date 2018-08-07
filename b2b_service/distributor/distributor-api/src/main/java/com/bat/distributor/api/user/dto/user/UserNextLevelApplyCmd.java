package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserNextLevelApplyCmd", description = "下级分销商注册信息")
public class UserNextLevelApplyCmd {

    @NotNull(message = "P_DISTRIBUTOR_USER_NEXT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "上级分销商id", required = true, example = "bat")
    private Integer distributorId;
    @NotBlank(message = "P_DISTRIBUTOR_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String name;
    @NotBlank(message = "P_DISTRIBUTOR_PASSWORD_NULL")
    @ApiModelProperty(value = "登录密码", required = true, example = "bat")
    private String password;
    @ApiModelProperty(value = "商户名称", required = false, example = "bat")
    private String companyName;
    @NotBlank(message = "P_DISTRIBUTOR_CONTACTS_NAME_NULL")
    @ApiModelProperty(value = "商户联系人名称", required = true, example = "bat")
    private String accountName;
    @ApiModelProperty(value = "商户联系人手机号", required = false, example = "bat")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", required = false, example = "1244@163.com")
    private String email;
}
