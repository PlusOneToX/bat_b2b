package com.bat.system.api.logistics.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 9:48
 */
@Data
public class FormulaCheckCmd {
    @NotBlank(message = "P_LOGISTICS_FORMULA_NULL")
    @ApiModelProperty(value = "公式", example = "{{1000-p}-0.6}*(4+[(w-1000)/1000]*1.5)")
    private String formula;

    // @NotNull(message = "P_LOGISTICS_WEIGHT_NULL")
    @ApiModelProperty(value = "重量", example = "1")
    private Double weight = 0.0;

    @NotNull(message = "P_LOGISTICS_PRICE_NULL")
    @ApiModelProperty(value = "价格", example = "1")
    private Double price;

    // @NotNull(message = "P_LOGISTICS_VOLUME_NULL")
    @ApiModelProperty(value = "体积", example = "1")
    private Double volume = 0.0;
}
