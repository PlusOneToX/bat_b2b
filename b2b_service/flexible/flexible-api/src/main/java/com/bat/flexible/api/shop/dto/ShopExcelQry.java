package com.bat.flexible.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopExcelQry {

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "分销商编码/门店名称/门店编码")
    private String content;

    @ApiModelProperty(value = "门店名称")
    private String shopName;


    @ApiModelProperty(value = "门店编码")
    private String shopCode;

    @ApiModelProperty(value = "公众号名称")
    private String appName;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
}
