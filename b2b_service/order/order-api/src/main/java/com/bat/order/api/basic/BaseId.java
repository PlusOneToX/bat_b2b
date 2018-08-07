package com.bat.order.api.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "BaseId", description = "id")
public class BaseId {
    @NotNull(message = "P_ORDER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "123445")
    private Integer id;
}
