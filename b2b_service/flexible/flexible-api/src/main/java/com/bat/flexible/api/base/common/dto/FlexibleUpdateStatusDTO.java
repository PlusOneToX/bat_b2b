package com.bat.flexible.api.base.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "启用禁用通用接口对象")
public class FlexibleUpdateStatusDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "FLEXIBLE_ID_NULL")
    private Integer id;


    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

}
