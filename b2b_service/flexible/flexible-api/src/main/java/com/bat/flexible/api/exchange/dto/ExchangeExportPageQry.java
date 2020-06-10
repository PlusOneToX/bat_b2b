package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeExportPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "兑换卡活动id")
    private Integer exchangeId;

}
