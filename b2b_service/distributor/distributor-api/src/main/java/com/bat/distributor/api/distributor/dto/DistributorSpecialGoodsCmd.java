package com.bat.distributor.api.distributor.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商特价商品信息")
public class DistributorSpecialGoodsCmd {

    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品id", required = true, example = "1234")
    private Integer goodsId;
    @NotNull(message = "P_DISTRIBUTOR_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "货品id", required = true, example = "1234")
    private Integer goodsItemId;
    @NotNull(message = "P_DISTRIBUTOR_SPECIAL_PRICE_NULL")
    @ApiModelProperty(value = "商品特价", required = true, example = "12.987")
    private BigDecimal distributorPrice;
    @NotNull(message = "P_DISTRIBUTOR_SPECIAL_PRICE_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型 1 新增 2 修改 3 删除", required = true, example = "1")
    private Short operationType;
}
