package com.bat.warehouse.api.setting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WarehouseSettingCmd {

    private Integer id;


    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型 1、库存同步 2、在途交期",required = true)
    private Short type;

    @NotNull(message = "值不能为空")
    @ApiModelProperty(value = "输入框值",required = true)
    private Integer value;

}
