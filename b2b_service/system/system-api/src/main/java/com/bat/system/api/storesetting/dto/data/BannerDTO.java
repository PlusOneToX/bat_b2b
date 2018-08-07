package com.bat.system.api.storesetting.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BannerDTO")
public class BannerDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "推广图片地址")
    private String imageUrl;
    @ApiModelProperty(value = "推广链接地址")
    private String bannerUrl;
    @ApiModelProperty(value = "推广区域 0-国内 1 海外")
    private Short bannerArea;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}