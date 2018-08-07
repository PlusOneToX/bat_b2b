package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品erp等级价格信息")
public class GoodsItemErpPriceDTO {
    @ApiModelProperty(value = "货品价格等级id", required = true, example = "1345")
    private Integer goodsItemGradeId;
    @ApiModelProperty(value = "货品等级价格")
    private BigDecimal price;
}
