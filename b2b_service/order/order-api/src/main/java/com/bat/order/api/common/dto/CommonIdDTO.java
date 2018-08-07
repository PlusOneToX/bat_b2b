package com.bat.order.api.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommonIdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "COMMON_ID_NULL")
    private Integer id;

}
