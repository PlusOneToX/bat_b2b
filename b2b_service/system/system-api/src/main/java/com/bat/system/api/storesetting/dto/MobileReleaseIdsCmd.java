package com.bat.system.api.storesetting.dto;

import java.util.List;

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
@ApiModel(value = "MobileReleaseIdsCmd", description = "移动端首页发布")
public class MobileReleaseIdsCmd {
    @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页id", required = true, example = "1")
    private List<Integer> ids;

    @NotNull(message = "P_MOBILE_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "发布状态", required = true, example = "1")
    private Short releaseStatus;
}
