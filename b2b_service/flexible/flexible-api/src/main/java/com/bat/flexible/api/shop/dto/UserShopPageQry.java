package com.bat.flexible.api.shop.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserShopPageQry extends ShopPageQry {

    @ApiModelProperty(value = "门店名称")
    private String shopName;


    @ApiModelProperty(value = "门店编码")
    private String shopCode;

    @ApiModelProperty(value = "公众号名称")
    private String appName;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

}
