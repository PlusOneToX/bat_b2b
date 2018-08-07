package com.bat.system.api.storesetting.dto;

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
@ApiModel(value = "MobileItemCmd", description = "移动端首页设置新增")
public class MobileItem {

    private Integer id;

    // @NotNull(message = "P_MOBILE_IMAGE_URL_NULL")
    @ApiModelProperty(value = "移动端首页图片url", required = true, example = "https://file.limlim.cn/bat/banner1.png")
    private String imageUrl;

    @ApiModelProperty(value = "图片宽度百分百", required = false, example = "50.00")
    private BigDecimal widthPercentage;

    // @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页跳转类型", required = true, example = "1")
    private Short jumpType;

    // @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页跳转参数", required = true, example = "测试")
    private String jumpParams;

    // @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页内容排序", required = true, example = "1")
    private Integer subSort;

    // @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页推荐样式", required = true, example = "0")
    private Short styleType;
}
