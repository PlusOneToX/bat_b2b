package com.bat.financial.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderSubAccountBillAmountCmd {

    @NotNull(message = "COMMON_ID_NULL")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "实际分账金额")
    @NotNull(message = "COMMON_AMOUNT_NULL")
    @Min(value = 0L,message = "COMMON_NUMBER_ILLEGAL")
    private BigDecimal actualSubAccountAmount;

}
