package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistributorScalePriceDTO", description = "一级分销商价格等级关系数据")
public class DistributorOneScalePriceDTO {
    @ApiModelProperty(value = "品牌价格等级id", example = "0")
    private Integer id;
    @ApiModelProperty(value = "品牌id,默认等级为0", example = "0")
    private Integer brandId;
    @ApiModelProperty(value = "品类id,默认等级为0,品牌等级也为0", example = "0")
    private Integer categoryId;
    @ApiModelProperty(value = "价格等级id", example = "0")
    private Integer scalePriceId;
    @ApiModelProperty(value = "默认分销模式价格等级", example = "11252")
    private Integer distributionScalePriceId;
}