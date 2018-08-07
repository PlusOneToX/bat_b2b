package com.bat.system.api.trainingdownloadcenter.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 16:47
 */
@Data
@ApiModel(value = "DownloadCenterUpdateCmd", description = "下载中心更新")
public class DownloadCenterUpdateCmd {

    @NotNull(message = "P_DOWNLOAD_CENTER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    // @NotNull(message = "P_DOWNLOAD_CENTER_PARENT_ID_NULL")
    @ApiModelProperty(value = "父id", required = true, example = "1")
    private Integer parentId;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "中文标题", example = "1")
    private String titleZh;

    @ApiModelProperty(value = "英文标题", example = "1")
    private String titleEn;

    @ApiModelProperty(value = "内容url中文", example = "1")
    private String contentUrlZh;

    @ApiModelProperty(value = "内容url英文", example = "1")
    private String contentUrlEn;

    @ApiModelProperty(value = "缩略图(中文)", example = "1")
    private String thumbnailUrlZh;

    @ApiModelProperty(value = "缩略图(英文)", example = "1")
    private String thumbnailUrlEn;

}
