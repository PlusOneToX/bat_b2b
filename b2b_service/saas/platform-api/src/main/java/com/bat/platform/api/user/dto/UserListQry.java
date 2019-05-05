package com.bat.platform.api.user.dto;

import java.io.Serializable;

import com.bat.platform.api.base.PageQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
@Data
@ApiModel(value = "UserListQry", description = "平台用户查询")
public class UserListQry extends PageQry implements Serializable {
    @ApiModelProperty(value = "状态, 1.启用 0.禁用", required = false, example = "123343")
    private Short openFlag;
    @ApiModelProperty(value = "搜索类型：1 账号 2 姓名 3 手机号 4 邮箱", required = false, example = "123343")
    private Short contentType;
    @ApiModelProperty(value = "搜索内容，账号/姓名/手机号/邮箱", required = false, example = "123343")
    private String content;
}
