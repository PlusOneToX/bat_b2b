package com.bat.system.api.organization.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "DepartmentQry", description = "销售部门查询")
public class DepartmentQry extends BaseSearchQry {

    @ApiModelProperty(value = "销售部门名称", example = "中国区事业部")
    private String departmentName;

    @ApiModelProperty(value = "是否销售部门", example = "1")
    private Short saleType;

    @ApiModelProperty(value = "销售部门所属组织", example = "100")
    private Integer organizationId;
}
