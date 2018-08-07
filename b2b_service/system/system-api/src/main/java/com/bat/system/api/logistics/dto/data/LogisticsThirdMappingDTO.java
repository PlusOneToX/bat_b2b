package com.bat.system.api.logistics.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/3 14:06
 */
@Data
@ApiModel(value = "LogisticsThirdMappingDTO")
public class LogisticsThirdMappingDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "物流id")
    private Integer logisticsId;
    @ApiModelProperty(value = "第三方类型 1、麦客")
    private Short thirdType;
    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short status;
    @ApiModelProperty(value = "第三方配送编号")
    private String thirdDeliveryNo;
    @ApiModelProperty(value = "备注")
    private String remark;
}
