package com.bat.flexible.dao.shop.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ShopPageCO {

    @ApiModelProperty(value = "门店id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "分销商所属公司")
    private String distributorCompanyName;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "门店编码")
    private String shopCode;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "微信url")
    private String url;

    @ApiModelProperty(value = "二维码")
    private String qrUrl;


    @ApiModelProperty(value = "第三方二维码")
    private String thirdQrUrl;

    @ApiModelProperty(value = "app名称")
    private String appName;

    @ApiModelProperty(value = "微信平台id")
    private Integer wxPlatformId;

    @ApiModelProperty(value = "拓展参数、第三方规则")
    private String extendParam;

    @ApiModelProperty(value = "关联的平台类型 1、微信公众号 2、微信小程序")
    private Short type;

    @ApiModelProperty(value = "平台编码(订单来源)、微信平台会携带")
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
