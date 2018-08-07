package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 12:59
 */
@Data
@ApiModel(value = "NoticeReleaseCmd", description = "公告发布")
public class NoticeReleaseCmd {
    @NotNull(message = "P_NOTICE_ID_NULL")
    @ApiModelProperty(value = "公告id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_NOTICE_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "发布状态", required = true, example = "1")
    private Short releaseStatus;
}
