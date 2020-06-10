package com.bat.flexible.api.base.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FlexibleIdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "FLEXIBLE_ID_NULL")
    private Integer id;

}
