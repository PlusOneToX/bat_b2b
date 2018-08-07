package com.bat.system.api.organization.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "DepartmentDTO")
public class DepartmentDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    @ApiModelProperty(value = "组织id")
    private Integer organizationId;
    @ApiModelProperty(value = "组织名称")
    private String organizationName;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "部门名称英文")
    private String departmentNameEn;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "erp系统中的销售部门id")
    private String erpDepartmentId;
    @ApiModelProperty(value = "部门描述")
    private String description;
    @ApiModelProperty(value = "0 禁用 1启用")
    private Short saleType;
    @ApiModelProperty(value = "0 禁用 1启用")
    private Short status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
