package com.bat.system.api.organization.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "DepartmentUpdateCmd", description = "销售部门更新")
public class DepartmentUpdateCmd {

    @NotNull(message = "P_DEPARTMENT_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "销售部门所属组织", required = true, example = "100")
    private Integer organizationId;

    @NotBlank(message = "P_DEPARTMENT_NAME_NULL")
    @ApiModelProperty(value = "销售部门名称", required = true, example = "中国区事业部")
    private String departmentName;

    @ApiModelProperty(value = "销售部门英文名称", example = "China Business Group")
    private String departmentNameEn;

    @NotBlank(message = "P_ERP_DEPARTMENT_ID_NULL")
    @ApiModelProperty(value = "ERP销售部门ID", required = true, example = "RQ201807015")
    private String erpDepartmentId;

    @ApiModelProperty(value = "销售部门描述")
    private String description;

    @NotNull(message = "P_SALE_TYPE_NULL")
    @ApiModelProperty(value = "是否销售部门", required = true, example = "1")
    private Short saleType;
}
