package com.bat.distributor.api.user.dto.customer.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户信息")
public class CustomerDTO {
    @ApiModelProperty(value = "客户id", example = "123444")
    private Integer id;
    @ApiModelProperty(value = "客户编号", example = "123444")
    private String no;
    @ApiModelProperty(value = "客户手机号", example = "18650811111")
    private String phone;
    @ApiModelProperty(value = "客户名称", example = "bat")
    private String userName;
    @ApiModelProperty(value = "密码", example = "787988235443")
    private String password;
    @ApiModelProperty(value = "客户昵称", example = "bat")
    private String nikeName;
    @ApiModelProperty(value = "性别 1为男性，2为女性", example = "1")
    private Short sex;
    @ApiModelProperty(value = "客户生日", example = "2018-05-09")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    @ApiModelProperty(value = "头像", example = "http://bat")
    private String headPortrait;
    @ApiModelProperty(value = "确认协议:1 已确认 0未确认", example = "0")
    private Short agreementFlag;
    @ApiModelProperty(value = "此用户是否已经被冻结 0为否,1为已冻结", example = "0")
    private Short status;
    @ApiModelProperty(value = "用户类型 1、有效用户(有手机号码)  2、游客（没有填写手机号码）", example = "1")
    private Short customerType;

}
