package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DistributorPriceQry {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "材质id")
    @NotNull(message = "COMMON_MATERIAL_ID_NULL")
    private Integer materialId;

    @ApiModelProperty(value = "订单来源")
    @NotNull(message = "COMMON_ORDER_SOURCE_NULL")
    private String orderSource;
}
