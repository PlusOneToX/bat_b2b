package com.bat.financial.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderSubAccountBillBatchCmd {

    @Valid
    @NotNull(message = "COMMON_LIST_NULL")
    @ApiModelProperty(value = "流水金额列表")
    private List<OrderSubAccountBillAmountCmd> billAmountCmdList;


}
