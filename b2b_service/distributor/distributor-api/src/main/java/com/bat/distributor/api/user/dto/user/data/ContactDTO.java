package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "联系人登录信息")
public class ContactDTO {

    @ApiModelProperty(value = "联系人id", example = "12334")
    private Integer id;
    @ApiModelProperty(value = "联系人名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "手机号登录密码(HD5加密)", example = "bat")
    private String password;
    @ApiModelProperty(value = "性别 0,未设置, 1,男 2,女", example = "1")
    private Short sex;
    @ApiModelProperty(value = "登录手机号", example = "bat")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", example = "1244@163.com")
    private String email;
    @ApiModelProperty(value = "是否账号拥有: 0 否, 1 是", example = "0")
    private Short ownerFlag;
    @ApiModelProperty(value = "联系人角色id", example = "1")
    private Integer roleId;

    @ApiModelProperty(value = "联系人openId")
    private String openId;
}
