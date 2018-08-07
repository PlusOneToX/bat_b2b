package com.bat.system.api.logistics.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/25 21:44
 */
@Data
public class LogisticsAvailableQry {

    @ApiModelProperty(value = "国家", required = true, example = "1")
    private Integer countryId;

    @ApiModelProperty(value = "省", required = true, example = "1")
    private Integer provinceId;

    @ApiModelProperty(value = "市", required = true, example = "1")
    private Integer cityId;

    @ApiModelProperty(value = "区", required = true, example = "1")
    private Integer districtId;

    @ApiModelProperty(value = "适用范围", required = true, example = "1")
    protected String useRange;

    @ApiModelProperty(value = "生产商 YC.云创 LHW.联辉王（同时支持，中间用\",\"号隔开）", required = true, example = "1")
    private String manufactors;

    @ApiModelProperty(value = "客户id", required = true, example = "1")
    protected String distributorId;

}
