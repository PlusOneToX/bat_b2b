package com.bat.distributor.api.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DistributorOpenFlagCmd {

    @ApiModelProperty(value = "id")
    @NotNull(message = "COMMON_ID_NULL")
    private Integer id;


    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "D_COMMON_OPEN_FLAG_NULL")
    private Short openFlag;
}
