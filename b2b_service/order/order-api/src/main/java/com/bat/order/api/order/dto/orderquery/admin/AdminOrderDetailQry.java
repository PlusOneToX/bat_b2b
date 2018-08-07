package com.bat.order.api.order.dto.orderquery.admin;

import javax.validation.constraints.NotNull;

import com.bat.order.api.basic.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单列表")
public class AdminOrderDetailQry extends BaseId {
    @NotNull(message = "P_SEARCH_TYPE_NULL")
    @ApiModelProperty(value = "查看常量确定", example = "1")
    private Short searchType;
}
