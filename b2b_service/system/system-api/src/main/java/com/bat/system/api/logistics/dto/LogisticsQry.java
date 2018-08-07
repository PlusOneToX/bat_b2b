package com.bat.system.api.logistics.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "LogisticsQry", description = "配送查询")
public class LogisticsQry extends BaseSearchQry {
    @ApiModelProperty(value = "配送名称", example = "百世快递（在线定制专用）")
    private String name;

    @ApiModelProperty(value = "生产商 YC.云创 LHW.联辉王（同时支持，中间用\",\"号隔开）", example = "1")
    private String manufactors;

    @ApiModelProperty(value = "是否启用", example = "1")
    private Short enable;

    @ApiModelProperty(value = "国家id", example = "37")
    private Integer countryId;

    @ApiModelProperty(value = "省id", example = "440000")
    private Integer provinceId;

    @ApiModelProperty(value = "城市id", example = "440300")
    private Integer cityId;

    @ApiModelProperty(value = "地区id(可以不传 传了也不会用)", example = "440307")
    private Integer districtId;
}
