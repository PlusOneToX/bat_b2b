package com.bat.distributor.api.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CategoryAddCmd", description = "分销商类别信息")
public class CategoryCmd {

    @ApiModelProperty(value = "分销商类别ID", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_NAME_NULL")
    @ApiModelProperty(value = "分销商类别名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商类别描述", required = false, example = "bat")
    private String description;
    @ApiModelProperty(value = "ERP分销商类别编号", required = false, example = "bat")
    private String erpCategoryNo;
    @NotNull(message = "P_DISTRIBUTOR_ORDER_TYPE_NULL")
    @ApiModelProperty(value = "订单类型", required = true, example = "14565")
    private Integer orderTypeId;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
