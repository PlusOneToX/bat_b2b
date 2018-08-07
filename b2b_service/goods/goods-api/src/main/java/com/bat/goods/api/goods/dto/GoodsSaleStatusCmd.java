package com.bat.goods.api.goods.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsSaleStatusCmd", description = "商品上下架")
public class GoodsSaleStatusCmd extends GoodsId {

    @NotNull(message = "P_GOODS_SALE_STATUS")
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", required = true, example = "3")
    private Short saleStatus;

}
