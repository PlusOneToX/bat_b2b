package com.bat.distributor.api.customer.dto;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户列表查询")
public class CustomerListQry extends BasePage {
    @ApiModelProperty(value = "查询内容，客户手机号/客户名称/客户昵称，不支持模糊查询", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型，1 客户手机号 2 客户名称 3 客户昵称", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "性别 1为男性，2为女性", required = false, example = "1")
    private Short sex;
    @ApiModelProperty(value = "用户类型 1、有效用户(有手机号码)  2、游客（没有填写手机号码）", required = false, example = "1")
    private Short customerType;
    @ApiModelProperty(value = "注册来源编码(分销商平台编码)", required = false, example = "1")
    private String platform;
}
