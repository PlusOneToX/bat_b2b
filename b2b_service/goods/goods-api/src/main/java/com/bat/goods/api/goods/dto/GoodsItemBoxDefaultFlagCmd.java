package com.bat.goods.api.goods.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsItemBoxDefaultFlagCmd", description = "货品装箱信息")
public class GoodsItemBoxDefaultFlagCmd {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "货品装箱id", required = true, example = "123343")
    private Integer id;
    @NotNull(message = "P_GOODS_BOX_DEFAULT_FLAG_NULL")
    @ApiModelProperty(value = "是否按装箱数量售卖 1、是 0、否", required = true, example = "1")
    private Short defaultFlag;
}
