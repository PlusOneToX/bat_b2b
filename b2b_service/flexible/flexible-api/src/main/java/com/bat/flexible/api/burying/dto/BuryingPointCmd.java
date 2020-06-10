package com.bat.flexible.api.burying.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BuryingPointCmd  {

    @ApiModelProperty(value = "来源")
    @NotNull(message = "COMMON_ORDER_SOURCE_NULL")
    private String source;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "网络类型 1.未知 2.wifi 3.3g 4.4g 5.5g")
    @NotNull(message = "B_NETWORK_TYPE_NULL")
    private Short networkType;


}
