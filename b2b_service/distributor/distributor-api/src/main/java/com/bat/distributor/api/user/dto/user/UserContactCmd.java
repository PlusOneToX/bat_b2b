package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "联系人信息")
public class UserContactCmd {

    @ApiModelProperty(value = "联系人id（修改时必填）", required = false, example = "bat2018")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商（客户）id", required = true, example = "bat")
    private Integer distributorId;
    @NotBlank(message = "P_DISTRIBUTOR_CONTACTS_NAME_NULL")
    @ApiModelProperty(value = "联系人名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "手机号登录密码(HD5加密)", required = false, example = "bat")
    private String password;
    @NotNull(message = "P_DISTRIBUTOR_SEX_NULL")
    @ApiModelProperty(value = "性别 0,未设置, 1,男 2,女", required = true, example = "1")
    private Short sex;
    @ApiModelProperty(value = "联系人手机号", required = false, example = "bat")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", required = false, example = "1244@163.com")
    private String email;
    @NotNull(message = "P_DISTRIBUTOR_CONTACT_OWNER_FLAG_NULL")
    @ApiModelProperty(value = "是否账号拥有: 0 否, 1 是", required = true, example = "0")
    private Short ownerFlag;
    @ApiModelProperty(value = "联系人角色id", required = false, example = "1")
    private Integer roleId;
    @NotNull(message = "P_DISTRIBUTOR_CONTACT_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型 1 新增 2 修改", required = true, example = "1")
    private Short operationType;
}
