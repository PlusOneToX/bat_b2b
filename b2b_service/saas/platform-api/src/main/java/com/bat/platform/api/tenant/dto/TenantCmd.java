package com.bat.platform.api.tenant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "租户添加或修改")
public class TenantCmd {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotNull(message = "P_PLATFORM_COMPANY_TYPE_NULL")
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人")
    private Short companyType;

    @NotBlank(message = "P_PLATFORM_COMPANY_NAME_NULL")
    @ApiModelProperty(value = "公司名")
    private String companyName;

    @NotBlank(message = "P_PLATFORM_REAL_NAME_NULL")
    @ApiModelProperty(value = "联系人姓名")
    private String realName;

    @NotNull(message = "P_PLATFORM_SEX_NULL")
    @ApiModelProperty(value = "性别 0,未设置, 1,男 2,女")
    private Short sex;

    @NotBlank(message = "P_PLATFORM_MOBILE_NULL")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotNull(message = "P_PLATFORM_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1.启用 0.禁用")
    private Short openFlag;

    @ApiModelProperty(value = "备注(可以填禁用原因)")
    private String remark;

}