package com.bat.flexible.api.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "门店条件查询对象")
public class ShopListQry {

    @ApiModelProperty(value = "分销商端分账配置id")
    private Integer userConfigId;


}
