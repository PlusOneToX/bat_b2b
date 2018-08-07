package com.bat.order.api.order.dto.orderquery.common;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/28 17:26
 */
@Data
public class OrderTypeDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单类型名称")
    private String name;
    @ApiModelProperty("erp订单类型")
    private String erpType;
    @ApiModelProperty("特殊类型, 1 普通,2 mto,3 现款,4 定制,5 直运")
    private Short specialFlag;
    @ApiModelProperty("默认仓库id")
    private Integer warehouseId;
    @ApiModelProperty("订单类型描述")
    private String description;
    @ApiModelProperty("是否可编辑：1-可编辑 0-不可编辑")
    private Short editable;
    @ApiModelProperty("是否启用, 0为停用，1为启用")
    private Short openFlag;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
