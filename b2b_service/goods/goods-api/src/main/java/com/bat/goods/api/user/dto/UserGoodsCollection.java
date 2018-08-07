package com.bat.goods.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserGoodsCollection", description = "删除收藏商品")
public class UserGoodsCollection {

    @ApiModelProperty(value = "商品id，商品id不填时，清除所有收藏", required = true, example = "12424")
    private Integer id;
}
