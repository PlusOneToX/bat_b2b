package com.bat.flexible.dao.shop.dataobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ShopDO implements Serializable {
    private static final long serialVersionUID = 5424192586499384964L;

    @ApiModelProperty(value = "门店id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "门店编码")
    private String shopCode;

    private Integer wxPlatformId;

    @ApiModelProperty(value = "状态 1、启用0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否删除 1、已删除 0、未删除")
    private Short delFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String url;

    private String qrUrl;

    @ApiModelProperty(value = "拓展字段")
    private String extendParam;


    private String thirdQrUrl;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "微信平台名称")
    private String appName;

    @ApiModelProperty(value = "创建来源 1、B2B后台 2、B2B前台")
    private Short source;

    @ApiModelProperty(value = "关联的平台类型 1、微信公众号 2、微信小程序")
    private Short type;

    @ApiModelProperty(value = "平台编码(订单来源)、微信平台会携带")
    private String platform;

    @ApiModelProperty
    private String appSecret;

    @ApiModelProperty(value = "分账业务员id")
    private Integer salemanId;

    @ApiModelProperty(value = "分账业务员名称")
    private String salemanName;

    @ApiModelProperty(value = "分销商分账配置id")
    private Integer userConfigId;

    @ApiModelProperty(value = "分销商分账配置名称")
    private String userConfigName;
}