package com.bat.flexible.api.base.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "上下移动通用接口对象")
public class FlexibleUpOrDownDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "FLEXIBLE_ID_NULL")
    private Integer id;


    @ApiModelProperty(value = "true上移 false下移")
    @NotNull(message = "COMMON_UP_OR_DOWN_NULL")
    private Boolean flag;

}
