package com.bat.system.api.storesetting.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/7 20:38
 */
@Data
public class MobileQry extends BaseSearchQry {

    @ApiModelProperty(value = "移动端首页状态", example = "1")
    private Short status;

    @ApiModelProperty(value = "移动端首页模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块", example = "1")
    private Short moduleType;
}
