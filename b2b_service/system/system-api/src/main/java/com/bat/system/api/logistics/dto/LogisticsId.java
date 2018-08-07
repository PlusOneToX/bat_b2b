package com.bat.system.api.logistics.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "LogisticsId", description = "配送id")
public class LogisticsId {
    @NotNull(message = "P_LOGISTICS_ID_NULL")
    @ApiModelProperty(value = "配送方式id", required = true, example = "1")
    private Integer id;
}
