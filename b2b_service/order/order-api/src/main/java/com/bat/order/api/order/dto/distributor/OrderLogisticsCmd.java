package com.bat.order.api.order.dto.distributor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "配送方式信息")
public class OrderLogisticsCmd {
    @NotNull(message = "P_ORDER_LOGISTICS_TYPE_NULL")
    @ApiModelProperty(value = "配送方式类型：1 普通商品（标品） 2 定制工厂", required = false, example = "14545")
    private Short logisticsType;
    @ApiModelProperty(value = "生产商 YC云创 MK麦客 LHW联辉王（当配送方式类型为2时必填）", required = false, example = "14545")
    private String manufactors;
    @NotNull(message = "P_ORDER_LOGISTICS_ID_NULL")
    @ApiModelProperty(value = "配送方式id", required = true, example = "14545")
    private Integer logisticsId;
    @NotBlank(message = "P_ORDER_LOGISTICS_NAME_NULL")
    @ApiModelProperty(value = "配送方式名称", required = true, example = "14545")
    private String logisticsName;
}