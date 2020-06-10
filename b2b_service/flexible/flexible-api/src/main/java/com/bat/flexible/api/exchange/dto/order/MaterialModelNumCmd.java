package com.bat.flexible.api.exchange.dto.order;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MaterialModelNumCmd {

    @NotNull(message = "COMMON_MATERIAL_ID_NULL")
    private Integer materialId;

    @NotNull(message = "COMMON_MODEL_ID_NULL")
    private Integer modelId;

    @NotNull(message = "COMMON_QUANTITY_NULL")
    @Min(value = 1L,message = "COMMON_QUANTITY_MUST_GREATER_THEN_ZERO")
    private Integer num;
}
