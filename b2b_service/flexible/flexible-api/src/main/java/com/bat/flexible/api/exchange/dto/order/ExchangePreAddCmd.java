package com.bat.flexible.api.exchange.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExchangePreAddCmd {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "COMMON_USER_ID_NULL")
    private Integer userId;

    @NotEmpty(message = "F_ORDER_MATERIAL_MODEL_LIST_NULL")
    @Valid
    private List<MaterialModelNumCmd> materialModels;


}
