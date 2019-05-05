package com.bat.platform.api.base.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "D_COMMON_ID_ERROR")
    private Integer id;

}
