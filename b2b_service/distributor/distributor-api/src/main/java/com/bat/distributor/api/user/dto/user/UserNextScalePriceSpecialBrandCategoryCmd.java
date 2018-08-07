package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商价格等级特殊公式品牌品类关系信息")
public class UserNextScalePriceSpecialBrandCategoryCmd {
    @NotNull(message = "P_DISTRIBUTOR_NEXT_SCALE_PRICE_SPECIAL_BRAND_ID_NULL")
    @ApiModelProperty(value = "分销商品牌id", required = true, example = "78445")
    private Integer brandId;
    @ApiModelProperty(value = "分销商品类id", required = false, example = "78445")
    private Integer categoryId;
}