package com.bat.goods.api.scaleprice.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 */
@Data
@ApiModel(value = "ScalePriceOpenCmd", description = "价格等级停用启用")
public class ScalePriceOpenCmd extends ScalePriceId {
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
}
