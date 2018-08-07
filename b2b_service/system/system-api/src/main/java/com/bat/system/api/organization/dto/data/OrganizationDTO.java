package com.bat.system.api.organization.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:35
 */
@Data
@ApiModel(value = "OrganizationDTO")
public class OrganizationDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "组织名称")
    private String name;
    @ApiModelProperty(value = "ERP_组织代码")
    private String erpOrganizationId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "0 禁用 1 启用 2删除(目前物理删除)")
    private Short status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
