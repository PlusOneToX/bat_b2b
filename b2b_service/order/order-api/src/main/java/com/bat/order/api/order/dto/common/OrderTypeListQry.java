package com.bat.order.api.order.dto.common;

import com.bat.order.api.basic.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单类型列表")
public class OrderTypeListQry extends BaseSearchQry {

    public OrderTypeListQry() {
        super.attributeMapper.put((short)1, "setName");
        super.attributeMapper.put((short)2, "setErpType");
    }

    @ApiModelProperty(value = "1订单类型名称", example = "1")
    private String name;
    @ApiModelProperty(value = "2erp订单类型", example = "1")
    private String erpType;

}
