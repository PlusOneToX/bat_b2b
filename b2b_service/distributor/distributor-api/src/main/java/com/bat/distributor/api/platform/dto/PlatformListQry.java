package com.bat.distributor.api.platform.dto;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlatformListQry", description = "分销商平台列表表查询")
public class PlatformListQry extends BasePage {
    @ApiModelProperty(value = "查询内容，分销商平台名称，支持模糊查询", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = false, example = "0")
    private Short openFlag;
}
