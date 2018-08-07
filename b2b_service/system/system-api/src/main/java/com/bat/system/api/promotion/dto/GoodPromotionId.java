package com.bat.system.api.promotion.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/27 13:37
 */
@Data
@ApiModel(value = "GoodsId", description = "商品推广的id")
public class GoodPromotionId {

    @NotNull(message = "P_GOODS_PROMOTION_ID_NULL")
    @ApiModelProperty(value = "商品推广id", required = true, example = "12424")
    private Integer id;

}
