package com.bat.system.api.globalsetting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 20:23
 */
@Data
public class FactorySettingOrderInvalidUpdateCmd {
    @ApiModelProperty(value = "id", required = true, example = "0")
    private Integer id;

    @ApiModelProperty(value = "渠道名称", required = true, example = "0")
    private String name;

    @ApiModelProperty(value = "渠道来源", required = true, example = "0")
    private Integer orderSource;

    @ApiModelProperty(value = "订单时效", required = true, example = "0")
    private Integer invalid;
}
