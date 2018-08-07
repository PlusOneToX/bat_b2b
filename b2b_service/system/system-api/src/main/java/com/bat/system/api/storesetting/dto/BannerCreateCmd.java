package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "BannerCreateCmd", description = "推广新增")
public class BannerCreateCmd {

    @NotBlank(message = "P_IMAGE_URL_NULL")
    @ApiModelProperty(value = "图片url", required = true,
        example = "")
    private String imageUrl;

    @NotBlank(message = "P_BANNER_URL_NULL")
    @ApiModelProperty(value = "推广url", required = true,
        example = "")
    private String bannerUrl;

    @NotNull(message = "P_BANNER_AREA_NULL")
    @ApiModelProperty(value = "推广区域 0国内 1国外", required = true, example = "0")
    private Short bannerArea;

    @NotNull(message = "P_BANNER_SORT_NULL")
    @ApiModelProperty(value = "排序", example = "0")
    private Integer sort;

}
