package com.bat.distributor.api.platform.dto;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WxPlatformListQry", description = "分销商微信平台列表查询")
public class WxPlatformListQry extends BasePage {
    @ApiModelProperty(value = "查询内容", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型：1 平台名称 2 分销商名称", required = false, example = "0")
    private Short contentType;
    @ApiModelProperty(value = "平台类型：1 公众号 2 小程序", required = false, example = "1")
    private Short type;
    @ApiModelProperty(value = "分销商id", required = false, example = "bat")
    private Integer distributorId;
}
