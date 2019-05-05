package com.bat.platform.api.user.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户信息")
public class UserDTO {
    private Integer id;
    @ApiModelProperty(value = "账号", example = "bat")
    private String userName;
    @ApiModelProperty(value = "用户登录密码(HD5加密)", example = "bat")
    private String password;
    @ApiModelProperty(value = "姓名", example = "bat")
    private String realName;
    @ApiModelProperty(value = "手机号", example = "bat")
    private String mobile;
    @ApiModelProperty(value = "邮箱", example = "bat")
    private String email;
    @ApiModelProperty(value = "状态, 1.启用 0.禁用", example = "1")
    private Short openFlag;
    @ApiModelProperty(value = "备注(可以填禁用原因)", example = "1")
    private String remark;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}