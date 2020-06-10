package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeSpecialDistributorPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "专题id")
    private Integer exchangeSpecialId;
}
