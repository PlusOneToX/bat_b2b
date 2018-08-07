package com.bat.system.api.storesetting.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/6 16:32
 */
@Data
@ApiModel(value = "MobileItemDTO")
public class MobileItemDTO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "模块id")
    private Integer mobileId;

    @ApiModelProperty(value = "轮播图时为轮播图url 图片模块为图片url 商品推广模块时为背景url")
    private String imageUrl;

    @ApiModelProperty(value = "图片宽度百分百", example = "50.00")
    private BigDecimal widthPercentage;

    @ApiModelProperty(value = "0 无链接 1 跳转商品 2跳转分类 3跳转其他页面 4跳转搜索结果页")
    private Short jumpType;

    @ApiModelProperty(value = "跳转目标（跳转参数）")
    private String jumpParams;

    @ApiModelProperty(value = "子内容排序")
    private Integer subSort;

    @ApiModelProperty(value = "商品推广模块下：样式类型0 无效 1 3列商品 2 4列商品 3 多列商品")
    private Short styleType;
}
