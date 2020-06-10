package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeCardPageQry extends BasePageParamQry {


    //券码类型 1、专属码
    @ApiModelProperty(value = "券码类型 1、专属码")
    private Short type;

    //优惠类型
    private Short exchangeWay;

    //状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    @ApiModelProperty(value = "状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束")
    private Short status;

    //兑换码名称
    @ApiModelProperty(value = "兑换码名称")
    private String codeName;

    @ApiModelProperty(value = "是否生成实体卡 1、是 0、否")
    private Short isEntity;

}
