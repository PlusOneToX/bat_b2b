package com.bat.distributor.api.platform.dto;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SysPlatformListQry", description = "分销商系统平台列表查询")
public class SysPlatformListQry extends BasePage {
    @ApiModelProperty(value = "查询内容", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型：1 平台名称 2 分销商名称", required = false, example = "0")
    private Short contentType;
}
