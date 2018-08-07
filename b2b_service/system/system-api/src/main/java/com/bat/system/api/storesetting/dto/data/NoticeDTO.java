package com.bat.system.api.storesetting.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:03
 */
@Data
@ApiModel(value = "NoticeDTO")
public class NoticeDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "附件名称")
    private String attachmentName;
    @ApiModelProperty(value = "附件url")
    private String attachmentUrl;
    @ApiModelProperty(value = "发布地区 0 国内 1国外")
    private Short releaseArea;
    @ApiModelProperty(value = "发布状态，0,未发布，1 发布")
    private Short releaseStatus;
    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;
    @ApiModelProperty(value = "取消发布时间")
    private Date cancelTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}