package com.bat.flexible.api.shop.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "分销商编码/门店名称/门店编码")
    private String content;



}
