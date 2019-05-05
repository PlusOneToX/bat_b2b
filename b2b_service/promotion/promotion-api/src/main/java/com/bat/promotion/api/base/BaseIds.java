package com.bat.promotion.api.base;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseIds", description = "ids查询")
public class BaseIds {

    @NotNull(message = "P_PROMOTION_IDS_NULL")
    @ApiModelProperty(value = "ids", required = true)
    private List<Integer> ids;
}
