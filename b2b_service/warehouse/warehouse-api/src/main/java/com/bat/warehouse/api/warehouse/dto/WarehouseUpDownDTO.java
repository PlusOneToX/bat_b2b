package com.bat.warehouse.api.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class WarehouseUpDownDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "ID_NULL")
    private Integer id;

    @ApiModelProperty(value = "上移下移 true上移 false下移")
    @NotNull(message = "W_UP_OR_DOWN_NULL")
    private Boolean flag;

}
