package com.bat.financial.api.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FinancialIdDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "COMMON_ID_NULL")
    private Integer id;

}
