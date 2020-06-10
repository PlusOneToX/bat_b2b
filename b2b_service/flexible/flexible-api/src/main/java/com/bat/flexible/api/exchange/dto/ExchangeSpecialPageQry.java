package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeSpecialPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "所属平台")
    private Short activityPlatform;

    @ApiModelProperty(value = "活动类型")
    private Short type;

}
