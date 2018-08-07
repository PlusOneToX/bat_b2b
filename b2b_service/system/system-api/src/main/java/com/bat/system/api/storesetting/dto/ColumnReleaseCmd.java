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
@ApiModel(value = "ColumnReleaseCmd", description = "栏目发布")
public class ColumnReleaseCmd {
    @NotNull(message = "P_COLUMN_ID_NULL")
    @ApiModelProperty(value = "栏目id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_COLUMN_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "发布状态", required = true, example = "1")
    private Short releaseStatus;
}
