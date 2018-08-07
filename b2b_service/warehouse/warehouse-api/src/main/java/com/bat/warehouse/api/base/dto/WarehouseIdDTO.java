package com.bat.warehouse.api.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WarehouseIdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID_NULL")
    private Integer id;
}
