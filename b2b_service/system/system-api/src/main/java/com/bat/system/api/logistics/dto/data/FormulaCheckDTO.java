package com.bat.system.api.logistics.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 9:56
 */
@Data
@ApiModel(value = "LogisticsDTO")
public class FormulaCheckDTO {
    @ApiModelProperty(value = "费用")
    private String cost;
    @ApiModelProperty(value = "外币费用")
    private String foreignCost;
}
