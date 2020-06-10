package com.bat.flexible.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ShopCmd {

    @ApiModelProperty(value = "门店id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    @NotBlank(message = "COMMON_DISTRIBUTOR_NAME_NULL")
    private String distributorName;

    @ApiModelProperty(value = "分销商公司名称")
    @NotBlank(message = "COMMON_DISTRIBUTOR_COMPANY_NAME_NULL")
    private String distributorCompanyName;

    @ApiModelProperty(value = "门店名称")
    @NotNull(message = "S_SHOP_NAME_NULL")
    private String shopName;

    @ApiModelProperty(value = "门店编码")
    @NotBlank(message = "S_SHOP_CODE_NULL")
    private String shopCode;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "备注")
    private String remark;



    @ApiModelProperty(value = "二维码")
    private String qrUrl;


    @ApiModelProperty(value = "扩展参数")
    private String extendParam;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "appName")
    private String appName;

    @ApiModelProperty(value = "微信平台id")
    private Integer wxPlatformId;

    @ApiModelProperty(value = "关联的平台类型 1、微信公众号 2、微信小程序")
    @NotNull(message = "S_SHOP_TYPE_NULL")
    private Short type;

    @ApiModelProperty(value = "平台编码(订单来源)、微信平台会携带")
    @NotBlank(message = "COMMON_PLATFORM_NULL")
    private String platform;

    @ApiModelProperty(value = "分账业务员id")
    private Integer salemanId;

    @ApiModelProperty(value = "分账业务员名称")
    private String salemanName;

    @ApiModelProperty(value = "分销商分账配置id")
    private Integer userConfigId;

    @ApiModelProperty(value = "分销商分账配置名称")
    private String userConfigName;
}
