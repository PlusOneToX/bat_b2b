package com.bat.warehouse.api.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "BasePageParamQry",description = "分页基本参数、分页大小和当前页")
public class BasePageParamQry {


    @NotNull(message = "每页数量不能为空")
    @ApiModelProperty(value = "每页数量",required = true,example = "10")
    private Integer size;

    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码",required = true,example = "1")
    private Integer page;

}
