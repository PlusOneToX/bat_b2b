package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:02
 */
@Data
@ApiModel(value = "NoticeUpdateCmd", description = "公告更新")
public class NoticeUpdateCmd {

    @NotNull(message = "P_NOTICE_ID_NULL")
    @ApiModelProperty(value = "公告id", required = true, example = "1")
    private Integer id;

    @NotBlank(message = "P_NOTICE_TITLE_NULL")
    @ApiModelProperty(value = "公告标题", required = true, example = "1")
    private String title;

    // @NotBlank(message = "P_NOTICE_ATTACHMENT_NAME_NULL")
    @ApiModelProperty(value = "附件名称", example = "1")
    private String attachmentName;

    // @NotBlank(message = "P_NOTICE_ATTACHMENT_URL_NULL")
    @ApiModelProperty(value = "附件url", example = "1")
    private String attachmentUrl;

    @NotNull(message = "P_NOTICE_RELEASE_AREA_NULL")
    @ApiModelProperty(value = "发布区域", required = true, example = "1")
    private Short releaseArea;

    @NotNull(message = "P_NOTICE_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "发布状态", required = true, example = "1")
    private Short releaseStatus;

    @NotBlank(message = "P_NOTICE_CONTENT_NULL")
    @ApiModelProperty(value = "公告内容", required = true, example = "1")
    private String content;
}
