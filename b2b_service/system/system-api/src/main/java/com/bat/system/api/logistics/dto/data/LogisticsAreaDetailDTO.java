package com.bat.system.api.logistics.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/17 12:08
 */
@Data
@ApiModel(value = "LogisticsAreaDetailDTO")
public class LogisticsAreaDetailDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "物流费用id")
    private Integer logisticsCostId;

    @ApiModelProperty(value = "国家id")
    private Integer countryId;

    @ApiModelProperty(value = "省id")
    private Integer provinceId;

    @ApiModelProperty(value = "市id")
    private Integer cityId;

    @ApiModelProperty(value = "区id")
    private Integer districtId;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "区名称")
    private String districtName;

    private String regionName;

    private String regionNameEn;
}

