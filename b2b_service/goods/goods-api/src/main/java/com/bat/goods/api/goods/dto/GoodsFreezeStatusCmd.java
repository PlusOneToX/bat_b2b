package com.bat.goods.api.goods.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsFreezeStatusCmd", description = "商品冻结解冻")
public class GoodsFreezeStatusCmd extends GoodsId {

    @NotNull(message = "P_GOODS_FREEZE_STATUS")
    @ApiModelProperty(value = "冻结状态，1未冻结，2冻结", required = true, example = "1")
    private Short freezeStatus;
}
