package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
@ApiModel
public class ExchangeCardStatusCmd {
    //主键id
    @NotNull(message = "FLEXIBLE_ID_NULL")
    @ApiModelProperty(value = "id")
    private Integer id;

    //状态  0、草稿（初始化） 1、已发布 2、启用 3、停用 4、结束
    @NotNull(message = "EXCHANGE_CARD_STATUS_NULL")
    @ApiModelProperty(value = "状态  0、草稿（初始化） 1、已发布 2、启用 3、停用 4、结束")
    private Short status;


}
