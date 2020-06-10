package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MaterialTreeAdminQry {



    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "父节点ID、默认是0")
    private Integer parentId;


    @ApiModelProperty(value = "是否最终材质 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "搜索")
    private String content;

    @ApiModelProperty(value = "页码、不分页不用传")
    private Integer page;

    @ApiModelProperty(value = "页面大小")
    private Integer size;
}
