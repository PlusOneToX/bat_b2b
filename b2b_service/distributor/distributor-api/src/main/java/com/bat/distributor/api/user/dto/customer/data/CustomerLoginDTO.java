package com.bat.distributor.api.user.dto.customer.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户信息")
public class CustomerLoginDTO {
    @ApiModelProperty(value = "客户id", example = "123444")
    private Integer id;
    @ApiModelProperty(value = "客户编号", example = "123444")
    private String no;
    @ApiModelProperty(value = "客户手机号", example = "18650811111")
    private String phone;
    @ApiModelProperty(value = "第三方系统唯一标识码", example = "bat")
    private String openId;
    @ApiModelProperty(value = "第三方系统其他标识码(如的标识码)", example = "sfgregefqwew24356546523ffsf")
    private String otherId;
    @ApiModelProperty(value = "确认协议:1 已确认 0未确认", example = "0")
    private Short agreementFlag;
    @ApiModelProperty(value = "此用户是否已经被冻结 0为否,1为已冻结", example = "0")
    private Short status;
    @ApiModelProperty(value = "平台归属用户是否已经被冻结 0为否,1为已冻结", example = "0")
    private Short platformStatus;
    @ApiModelProperty(value = "客户名称", example = "bat")
    private String userName;
    @ApiModelProperty(value = "密码", example = "787988235443")
    private String password;
    @ApiModelProperty(value = "用户类型 1、有效用户(有手机号码)  2、游客（没有填写手机号码）", example = "1")
    private Short customerType;
}
