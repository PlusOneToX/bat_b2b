package com.bat.flexible.api.exchange.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 徐益阳
 * @Description 后台兑换码解绑前端传值
 * @date 2019年04月25日 17:00
 */
@Data
public class ExchangeAdminUnboundCmd {
    @ApiModelProperty(value = "明码编号")
    @NotNull(message = "EXCHANGE_CARD_PLAIN_CODE_NULL")
    private String plainCode;

    @ApiModelProperty(value = "id编号")
    @NotNull(message = "EXCHANGE_CARD_CODE_NULL")
    private Integer id;
}
