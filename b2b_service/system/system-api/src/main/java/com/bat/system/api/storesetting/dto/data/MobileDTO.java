package com.bat.system.api.storesetting.dto.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:03
 */
@Data
@ApiModel(value = "MobileDTO")
public class MobileDTO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块")
    private Short moduleType;

    @ApiModelProperty(value = "0 隐藏 1显示")
    private Short status;

    @ApiModelProperty(value = "子内容")
    private List<MobileItemDTO> list;

    @ApiModelProperty(value = "子内容")
    List<MobileChildDTO> mobileChildDTOS;
}