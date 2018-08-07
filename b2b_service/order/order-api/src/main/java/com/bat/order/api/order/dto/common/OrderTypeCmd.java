package com.bat.order.api.order.dto.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/28 19:27
 */
@Data
public class OrderTypeCmd {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单类型名称")
    @NotBlank(message = "P_ORDER_TYPE_NAME_NULL")
    private String name;
    @NotBlank(message = "P_ORDER_TYPE_ERP_TYPE_NULL")
    @ApiModelProperty("erp订单类型")
    private String erpType;
    @NotNull(message = "P_ORDER_TYPE_SPECIAL_FLAG_NULL")
    @ApiModelProperty("特殊类型, 1 普通,2 mto,3 现款,4 定制,5 直运")
    private Short specialFlag;
    @NotNull(message = "P_ORDER_TYPE_WAREHOUSE_ID_NULL")
    @ApiModelProperty("默认仓库id")
    private Integer warehouseId;
    @ApiModelProperty("订单类型描述")
    private String description;
    @NotNull(message = "P_ORDER_TYPE_EDITABLE_NULL")
    @ApiModelProperty("是否可编辑：1-可编辑 0-不可编辑")
    private Short editable;
    @NotNull(message = "P_ORDER_TYPE_OPEN_FLAG_NULL")
    @ApiModelProperty("是否启用, 0为停用，1为启用")
    private Short openFlag;
}
