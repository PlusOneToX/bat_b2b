package com.bat.distributor.api.distributor.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商联系人信息")
public class DistributorContactsDTO {

    private Integer id;
    @ApiModelProperty(value = "联系人名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "手机号登录密码(HD5加密)", required = true, example = "bat")
    private String password;
    @ApiModelProperty(value = "性别 0,未设置, 1,男 2,女", required = true, example = "1")
    private Short sex;
    @ApiModelProperty(value = "联系人手机号", required = false, example = "bat")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", required = false, example = "1244@163.com")
    private String email;
    @ApiModelProperty(value = "是否账号拥有: 0 否, 1 是", required = true, example = "0")
    private Short ownerFlag;
    @ApiModelProperty(value = "联系人角色id", required = false, example = "1")
    private Integer roleId;
    @ApiModelProperty(value = "联系人角色名称", required = false, example = "1")
    private Integer roleName;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", required = true, example = "1")
    private Short freezeStatus;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
