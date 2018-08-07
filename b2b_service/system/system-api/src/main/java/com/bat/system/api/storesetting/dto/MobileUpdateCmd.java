package com.bat.system.api.storesetting.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/6 16:32
 */
@Data
@ApiModel(value = "MobileCreateCmd", description = "移动端首页设置新增")
public class MobileUpdateCmd {

    @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页id", example = "1")
    private Integer id;

    @NotNull(message = "P_MOBILE_SORT_NULL")
    @ApiModelProperty(value = "移动端首页排序值", example = "1")
    private Integer sort;

    @NotNull(message = "P_MOBILE_MODULE_TYPE_NULL")
    @ApiModelProperty(value = "移动端首页模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块", example = "1")
    private Short moduleType;

    @NotNull(message = "P_MOBILE_STATUS_NULL")
    @ApiModelProperty(value = "移动端首页状态", example = "1")
    private Short status;

    @ApiModelProperty(value = "商品列表分类")
    private List<MobileChildCmd> mobileChildCmds =new ArrayList<>();

    List<MobileItem> list;

    protected List<MobileGoodsItem> goodsIds;

}
