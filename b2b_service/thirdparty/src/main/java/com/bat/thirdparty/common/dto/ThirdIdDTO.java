package com.bat.thirdparty.common.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdIdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "COMMON_ID_NULL")
    private Integer id;
}
