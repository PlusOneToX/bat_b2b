package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 18:44
 */
@Data
@ApiModel(value = "MobileId", description = "移动端首页id")
public class MobileId {
    @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页id", required = true, example = "1")
    private Integer id;

}
