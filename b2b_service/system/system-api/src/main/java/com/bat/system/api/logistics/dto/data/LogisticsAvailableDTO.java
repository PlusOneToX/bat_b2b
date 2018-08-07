package com.bat.system.api.logistics.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 9:11
 */
@Data
@ApiModel(value = "LogisticsAvailableDTO")
public class LogisticsAvailableDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "物流名称")
    private String name;
    @ApiModelProperty(value = "物流描述")
    private String description;
}
