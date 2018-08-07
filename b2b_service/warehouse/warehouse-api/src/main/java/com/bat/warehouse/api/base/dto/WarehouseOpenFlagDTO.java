package com.bat.warehouse.api.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WarehouseOpenFlagDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID_NULL")
    private Integer id;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL_ERROR")
    private Short openFlag;
}
