package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExchangeCodeImportDTO {

    @ApiModelProperty(value = "是否生成实体卡 1、是 0、否")
    @NotNull(message = "EXCHANGE_CARD_IS_ENTITY_NULL")
    private Short isEntity;
}
