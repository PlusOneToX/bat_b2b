package com.bat.goods.api.goods.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品等级价格信息")
public class GoodsItemScalePriceCmd {

    private Integer id;
    @ApiModelProperty(value = "商品SPU id", required = false, example = "123343")
    private Integer goodsId;
    @ApiModelProperty(value = "货品SKU id", required = false, example = "123343")
    private Integer itemId;
    @NotNull(message = "P_GOODS_SCALE_ID_NULL")
    @ApiModelProperty(value = "价格等级id", required = true, example = "123343")
    private Integer goodsItemGradeId;
    @NotNull(message = "P_GOODS_SCALE_PRICE_NULL")
    @ApiModelProperty(value = "等级价格", required = true, example = "12.987")
    private BigDecimal price;

}
