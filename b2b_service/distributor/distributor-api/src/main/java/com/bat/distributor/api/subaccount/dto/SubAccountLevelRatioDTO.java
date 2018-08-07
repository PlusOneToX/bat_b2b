package com.bat.distributor.api.subaccount.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubAccountLevelRatioDTO {

    @ApiModelProperty(value = "分账等级id")
    @NotNull(message = "D_SUB_ACCOUNT_LEVEL_ID_NULL")
    private Integer levelId;

    @ApiModelProperty(value = "比例、百分比")
    @NotNull(message = "D_SUB_ACCOUNT_LEVEL_RATIO_NULL")
    @Min(value = 0L, message = "COMMON_QUANTITY_MUST_NOT_LESS_THEN_ZERO")
    @Max(value = 100L, message = "D_SUB_ACCOUNT_LEVEL_RATIO_MUST_NOT_GREATER_THEN_FIFTY")
    private BigDecimal ratio;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

}
