package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeGeneralItemQry extends BasePageParamQry {

    @ApiModelProperty(value = "模糊搜索80码")
    private String content;
}
