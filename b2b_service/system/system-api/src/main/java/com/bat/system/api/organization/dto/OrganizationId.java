package com.bat.system.api.organization.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:11
 */
@Data
@ApiModel(value = "OrganizationId", description = "组织id")
public class OrganizationId {
    @NotNull(message = "P_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;
}
