package com.bat.platform.api.tenant.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "平台租户公共配置信息")
public class TenantCommonDTO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "分销微信小程序授权密钥")
    private String wxProgramAppSecret;

    @ApiModelProperty(value = "分销微信小程序appid")
    private String wxProgramAppId;

    @ApiModelProperty(value = "兑换卡默认的分销商id")
    private Integer exchangeDistributorId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "主调色")
    private String colour;
}
