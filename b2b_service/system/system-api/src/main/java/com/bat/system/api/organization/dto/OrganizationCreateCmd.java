package com.bat.system.api.organization.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 19:34
 */
@Data
@ApiModel(value = "OrganizationCreateCmd", description = "组织新增")
public class OrganizationCreateCmd {

    @NotBlank(message = "P_ORGANIZATION_NAME_NULL")
    @ApiModelProperty(value = "组织名称", required = true, example = "深圳市bat卓越科技有限公司")
    private String name;

    @NotBlank(message = "P_ERP_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "ERP组织编码", required = true, example = "100")
    private String erpOrganizationId;

    @ApiModelProperty(value = "组织描述")
    private String description;
}
