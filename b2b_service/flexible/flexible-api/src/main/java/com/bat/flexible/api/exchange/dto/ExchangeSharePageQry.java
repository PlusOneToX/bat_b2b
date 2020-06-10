package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeSharePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "活动所属平台 1兑换商城 2定制商城")
    private Short activityPlatform;

    @ApiModelProperty(value = "减免优惠活动名称")
    private String preferName;

    @ApiModelProperty(value = "活动位置 1确认订单页 2订单详情页")
    private Short seat;

}
