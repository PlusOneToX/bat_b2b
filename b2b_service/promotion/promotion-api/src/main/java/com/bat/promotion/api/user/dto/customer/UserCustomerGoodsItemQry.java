package com.bat.promotion.api.user.dto.customer;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "提交订单的商品列表")
public class UserCustomerGoodsItemQry {
    @NotNull(message = "P_PROMOTION_MATERIAL_ID_NULL")
    @ApiModelProperty(value = "材质id", required = true, example = "1213")
    private Integer materialId;
    @NotNull(message = "P_PROMOTION_GOODS_PRICE_NULL")
    @ApiModelProperty(value = "商品价格", required = true, example = "1213")
    private BigDecimal price;
    @NotNull(message = "P_PROMOTION_GOODS_COUNT_NULL")
    @ApiModelProperty(value = "商品数量", required = true, example = "1213")
    private Integer count;
    @NotNull(message = "P_PROMOTION_MODEL_ID_NULL")
    @ApiModelProperty(value = "型号id", required = true, example = "1213")
    private Integer modelId;

}