package com.bat.goods.api.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserGoodsItemId", description = "商品id信息")
public class UserGoodsItemId {
    @NotNull(message = "P_GOODS_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品id", required = true, example = "12424")
    private Integer goodsId;
    @NotNull(message = "P_GOODS_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "货品id", required = true, example = "12424")
    private Integer itemId;
}
