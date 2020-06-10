package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExchangeShareQry {

    @NotNull(message = "EXCHANGE_ACTIVITY_PLATFORM_NULL")
    @ApiModelProperty(value = "活动所属平台 1兑换商城 2定制商城")
    private Short activityPlatform;

    @NotNull(message = "EXCHANGE_SEAT_NULL")
    @ApiModelProperty(value = "活动位置 1确认订单页 2订单详情页")
    private Short seat;

    @ApiModelProperty(value = "暗码")
    private List<String> secretCodeList;
}
