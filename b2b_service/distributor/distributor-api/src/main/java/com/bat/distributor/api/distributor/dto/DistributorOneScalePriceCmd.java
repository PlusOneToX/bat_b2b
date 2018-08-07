package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistributorOneScalePriceCmd", description = "一级分销商价格等级关系表")
public class DistributorOneScalePriceCmd {
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_ONE_SCALE_PRICE_BRAND_ID_NULL")
    @ApiModelProperty(value = "品牌id", required = true, example = "0")
    private Integer brandId;
    @ApiModelProperty(value = "品类id", required = true, example = "0")
    private Integer categoryId;
    @NotNull(message = "P_DISTRIBUTOR_ONE_SCALE_PRICE_ID_NULL")
    @ApiModelProperty(value = "价格等级id", required = true, example = "0")
    private Integer scalePriceId;
    @ApiModelProperty(value = "默认分销模式价格等级", required = false, example = "11252")
    private Integer distributionScalePriceId;
    @NotNull(message = "P_DISTRIBUTOR_SPECIAL_GOODS_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型 1 新增 2 修改 3 删除", required = true, example = "1")
    private Short operationType;
}