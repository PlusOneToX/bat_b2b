package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class ExchangeCodeUserPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "状态")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short status;

    @ApiModelProperty(value = "C端用户ID")
    @NotNull(message = "COMMON_USER_ID_NULL")
    private Integer userId;

    private Integer materialId;

    private Integer exchangeId;


}
