package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品等级价格信息封装")
public class GoodsItemScalePriceDTO {

    private Integer id;
    @ApiModelProperty(value = "商品SPU id", required = true, example = "123343")
    private Integer goodsId;
    @ApiModelProperty(value = "货品SKU id", required = true, example = "123343")
    private Integer goodsItemId;
    @ApiModelProperty(value = "价格等级id", required = true, example = "123343")
    private Integer goodsItemGradeId;
    @ApiModelProperty(value = "等级价格", required = true, example = "12.987")
    private BigDecimal price;

}
