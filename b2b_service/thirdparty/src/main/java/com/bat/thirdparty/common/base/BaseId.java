package com.bat.thirdparty.common.base;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "id")
public class BaseId {
    @NotNull(message = "P_ID_NULL")
    @ApiModelProperty(value = "id不能为空", required = true, example = "1223234")
    private Integer id;
}
