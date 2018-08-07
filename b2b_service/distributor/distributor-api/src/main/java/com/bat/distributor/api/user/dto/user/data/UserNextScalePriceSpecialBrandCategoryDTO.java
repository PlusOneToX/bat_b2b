package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级特殊公式品牌品类关系信息")
public class UserNextScalePriceSpecialBrandCategoryDTO {
    @ApiModelProperty(value = "id", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "分销商价格等级特殊公式id", example = "78445")
    private Integer nextScalePriceSpecialId;
    @ApiModelProperty(value = "分销商品牌id", example = "78445")
    private Integer brandId;
    @ApiModelProperty(value = "分销商品类id", example = "78445")
    private Integer categoryId;
}