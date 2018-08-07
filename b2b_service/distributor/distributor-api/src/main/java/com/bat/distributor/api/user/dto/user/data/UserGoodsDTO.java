package com.bat.distributor.api.user.dto.user.data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商商品列表")
public class UserGoodsDTO implements Serializable {

    private Integer id;
    @ApiModelProperty(value = "商品名称", example = "bat")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "bat")
    private String goodsNameEn;

}
