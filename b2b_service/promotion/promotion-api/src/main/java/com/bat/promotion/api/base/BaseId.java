package com.bat.promotion.api.base;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseId", description = "id查询")
public class BaseId {

    @NotNull(message = "P_PROMOTION_ID_NULL")
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    private String distributorId;
}
