package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeShareReleaseCmd {

    @ApiModelProperty(value = "发布id")
    private Integer id;

    @ApiModelProperty(value = "打开类型 1二次转发查看 2普通查看")
    private Short type;
}
