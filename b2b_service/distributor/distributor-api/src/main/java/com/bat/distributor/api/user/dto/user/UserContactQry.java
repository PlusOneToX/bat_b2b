package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserContactQry", description = "联系人查询")
public class UserContactQry {
    @ApiModelProperty(value = "手机号", example = "1")
    private String phone;
    @ApiModelProperty(value = "邮箱", example = "1")
    private String email;
}
