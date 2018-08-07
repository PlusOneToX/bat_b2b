package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/7 19:50
 */
@Data
@ApiModel(value = "MobileReleaseCmd", description = "移动端首页发布")
public class MobileReleaseCmd {
    @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_MOBILE_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "发布状态", required = true, example = "1")
    private Short releaseStatus;
}
