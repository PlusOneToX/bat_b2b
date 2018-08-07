package com.bat.system.api.organization.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:11
 */
@Data
@ApiModel(value = "OrganizationQry", description = "组织查询")
public class OrganizationQry extends BaseSearchQry {
    @ApiModelProperty(value = "组织名称", example = "深圳市bat卓越科技有限公司")
    private String name;
}
