package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubAccountSalemanCmd {

    @ApiModelProperty(value = "主键id、新增为空")
    private Integer id;

    @ApiModelProperty(value = "上级业务员、没有上级传0")
    @NotNull(message = "D_COMMON_PARENT_ID_NULL")
    private Integer parentId;

    @ApiModelProperty(value = "身份类型 1、企业 2、个人")
    @NotNull(message = "D_SUB_ACCOUNT_IDENTITY_TYPE_NULL")
    private Short type;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "D_COMMON_NAME_NULL")
    private String name;

    @ApiModelProperty(value = "手机号码不能为空")
    @NotBlank(message = "D_COMMON_MOBILE_NULL")
    private String mobile;

    @ApiModelProperty(value = "商户号")
    private String merchantNumber;

    @ApiModelProperty(value = "等级id")
    @NotNull(message = "D_SUB_ACCOUNT_LEVEL_ID_NULL")
    private Integer levelId;

}
