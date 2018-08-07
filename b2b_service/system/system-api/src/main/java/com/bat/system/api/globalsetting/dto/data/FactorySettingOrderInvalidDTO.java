package com.bat.system.api.globalsetting.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 20:10
 */
@Data
@ApiModel(value = "FactorySettingOrderInvalidDTO")
public class FactorySettingOrderInvalidDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "渠道名称")
    private String name;
    @ApiModelProperty(value = "渠道来源")
    private Integer orderSource;
    @ApiModelProperty(value = "订单时效")
    private Integer invalid;
}
