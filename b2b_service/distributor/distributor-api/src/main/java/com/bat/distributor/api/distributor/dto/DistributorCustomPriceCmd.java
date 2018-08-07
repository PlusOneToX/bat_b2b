package com.bat.distributor.api.distributor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;

import java.util.List;

@Data
public class DistributorCustomPriceCmd {



    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商价格列表")
    private List<DistributorCustomPriceListCmd> itemPriceList;


    @ApiModelProperty(value = "操作来源 1、B2B后台 2、B2B前台")
    @NotNull(message = "D_OPERATE_SOURCE_NULL")
    private Short source;

}
