package com.bat.flexible.api.exchange.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 徐益阳
 * @Description 兑换码解绑前端传值
 * @date 2019年04月25日 13:52
 */
@Data
public class ExchangeUnboundCmd {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "COMMON_USER_ID_NULL")
    private Integer userId;

    @ApiModelProperty(value = "兑换码id")
    @NotNull(message = "EXCHANGE_CARD_CODE_ID_NULL")
    private Integer exchangeCodeId;
}
