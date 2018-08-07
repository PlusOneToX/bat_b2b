package com.bat.distributor.api.distributor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DistributorCustomPriceListCmd {
    @ApiModelProperty(value = "主键id")
    private Integer id;


    @ApiModelProperty(value = "分销商价格")
    private BigDecimal price;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;
}
