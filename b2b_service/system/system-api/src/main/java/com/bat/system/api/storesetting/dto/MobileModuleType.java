package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/7 15:47
 */
@Data
@ApiModel(value = "MobileModuleType")
public class MobileModuleType {
    @NotNull(message = "P_MOBILE_MODULE_TYPE_NULL")
    @ApiModelProperty(value = "移动端首页模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块", required = true, example = "1")
    private Short moduleType;
}
