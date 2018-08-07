package com.bat.system.api.trainingdownloadcenter.dto.data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 15:42
 */
@Data
@ApiModel(value = "DownloadCenterDTO")
public class DownloadCenterDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "0禁用 1启用")
    private Short status;
    @ApiModelProperty(value = "标题 中文")
    private String titleZh;
    @ApiModelProperty(value = "标题 英文")
    private String titleEn;
    @ApiModelProperty(value = "内容 资源地址 中文")
    private String contentUrlZh;
    @ApiModelProperty(value = "内容 资源地址 英文")
    private String contentUrlEn;
    @ApiModelProperty(value = "缩略图 资源地址(未启用) 中文")
    private String thumbnailUrlZh;
    @ApiModelProperty(value = "缩略图 资源地址(未启用) 英文")
    private String thumbnailUrlEn;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "childrens")
    private List<DownloadCenterDTO> childrens;
}
