package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserApplyDTO", description = "分销商注册信息")
public class UserApplyDTO {

    @ApiModelProperty(value = "分销商id", example = "12334")
    private Integer id;
    @ApiModelProperty(value = "用户名(登录名)", example = "bat")
    private String userName;
    @ApiModelProperty(value = "登录密码", example = "bat")
    private String password;

}
