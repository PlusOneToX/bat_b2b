package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserOneLevelApplyCmd", description = "一级分销商注册信息")
public class UserOneLevelApplyCmd {

    @NotBlank(message = "P_DISTRIBUTOR_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String name;
    @NotBlank(message = "P_DISTRIBUTOR_PASSWORD_NULL")
    @ApiModelProperty(value = "登录密码", required = true, example = "bat")
    private String password;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", required = false, example = "1")
    private Short companyType;
    @ApiModelProperty(value = "公司名", required = false, example = "bat")
    private String companyName;
    @NotBlank(message = "P_DISTRIBUTOR_CONTACTS_NAME_NULL")
    @ApiModelProperty(value = "账户拥有联系人名称", required = true, example = "bat")
    private String accountName;
    @ApiModelProperty(value = "联系人手机号", required = false, example = "bat")
    private String phone;
    @ApiModelProperty(value = "联系人邮箱", required = false, example = "1244@163.com")
    private String email;
    @NotBlank(message = "P_DISTRIBUTOR_CONTACTS_CERT_NO_NULL")
    @ApiModelProperty(value = "营业执照或身份证号", required = true, example = "R00343")
    private String certNo;
    @ApiModelProperty(value = "分销商备注", required = false, example = "R00343")
    private String comment;

    @NotNull(message = "P_DISTRIBUTOR_COUNTRY_ID_NULL")
    @ApiModelProperty(value = "国家id", required = true, example = "1234")
    private Integer countryId;
    @ApiModelProperty(value = "省份id(国家为中国时必填)", required = false, example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id(国家为中国时必填)", required = false, example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "区id", required = false, example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "地址(当地址类型为2时必填)", required = false, example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "123455")
    private String zipCode;

}
