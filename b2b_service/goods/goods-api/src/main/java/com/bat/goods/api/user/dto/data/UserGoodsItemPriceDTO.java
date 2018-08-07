package com.bat.goods.api.user.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "前台货品(SKU)价格")
public class UserGoodsItemPriceDTO {

    @ApiModelProperty(value = "商品ID", example = "1345")
    private Integer goodsId;
    @ApiModelProperty(value = "货品ID", example = "1345")
    private Integer itemId;
    @ApiModelProperty(value = "建议零售价", example = "12.34")
    private BigDecimal retailPrice;
    @ApiModelProperty(value = "会员价", example = "12.34")
    private BigDecimal salePrice;
}
