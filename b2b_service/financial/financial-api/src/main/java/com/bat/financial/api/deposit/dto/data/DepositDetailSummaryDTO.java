package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/7 21:05
 */
@Data
public class DepositDetailSummaryDTO {
    @ApiModelProperty(value = "总收入")
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    @ApiModelProperty(value = "总支出")
    private BigDecimal totalExpenditure = BigDecimal.ZERO;
}
