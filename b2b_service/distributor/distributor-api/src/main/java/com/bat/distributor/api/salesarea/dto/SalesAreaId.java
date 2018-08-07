package com.bat.distributor.api.salesarea.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesAreaId", description = "销售区域id")
public class SalesAreaId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "销售区域id", required = true, example = "12424")
    private Integer id;
}
