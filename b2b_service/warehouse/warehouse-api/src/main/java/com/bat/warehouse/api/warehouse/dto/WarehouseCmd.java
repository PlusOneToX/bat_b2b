package com.bat.warehouse.api.warehouse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class WarehouseCmd {

    private Integer id;

    @NotBlank(message = "仓库名称不能为空")
    @ApiModelProperty(value = "仓库名称",required = true)
    private String name;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码",required = false)
    private String warehouseNo;

    @NotNull(message = "所属区域不能为空")
    @ApiModelProperty(value = "销售区域id",required = true)
    private Integer areaId;

    @NotNull(message = "是否集成不能为空")
    @Min(message = "最小值不能小于0",value = 0)
    @Max(message = "最大值不能小于0",value = 1)
    @ApiModelProperty(value = "是否集成",required = true,notes = "1、是 0、否")
    private Short syncType;

    @NotNull(message = "是否平台仓不能为空")
    @ApiModelProperty(value = "是否平台仓",required = true,notes = "1、是 0、否")
    @Min(message = "最小值不能小于0",value = 0)
    @Max(message = "最大值不能小于0",value = 1)
    private Short isPlatform;

    @NotNull(message = "是否参与存销比计算不能为空")
    @ApiModelProperty(value = "是否参与存销比计算",required = true,notes = "1、是 0、否")
    @Min(message = "最小值不能小于0",value = 0)
    @Max(message = "最大值不能小于0",value = 1)
    private Short calculationType;


    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态不能为空",required = true,notes = "1、是 0、否")
    @Min(message = "最小值不能小于0",value = 0)
    @Max(message = "最大值不能小于0",value = 1)
    private Short openFlag;

    private String remark;
}
