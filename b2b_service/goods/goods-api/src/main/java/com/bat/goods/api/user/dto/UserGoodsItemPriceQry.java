package com.bat.goods.api.user.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserGoodsItemPriceQry", description = "货品(SKU)价格查询")
public class UserGoodsItemPriceQry implements Serializable {
    @NotNull(message = "P_GOODS_GOODS_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "商品货品ids")
    private List<UserGoodsItemId> goodsItemIds;

}
