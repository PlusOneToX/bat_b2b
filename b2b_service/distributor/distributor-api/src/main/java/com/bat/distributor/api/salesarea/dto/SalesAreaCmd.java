package com.bat.distributor.api.salesarea.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesAreaAddCmd", description = "销售区域信息")
public class SalesAreaCmd {

    @ApiModelProperty(value = "销售区域id", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_SALES_AREA_NAME_NULL")
    @ApiModelProperty(value = "销售区域名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "销售区域描述", required = false, example = "bat")
    private String description;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
