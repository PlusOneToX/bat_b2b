package com.bat.system.api.promotion;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsPromotionId", description = "商品推广id信息")
public class GoodsPromotionId {

    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品推广id", required = true, example = "12424")
    private Integer id;
}
