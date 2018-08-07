package com.bat.order.api.importOrder.dto;

import com.bat.order.api.basic.BaseSearchQry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImportOrderListQry extends BaseSearchQry {
    @ApiModelProperty(value = "导入编号")
    private String importIds;
    @ApiModelProperty(value = "订单状态 1:已下单 2:未下单")
    private Short importOrderStatus = 0;
    @ApiModelProperty(value = "提交状态 1.未提交，2.提交中，3.提交失败")
    private Short submitStatus;
    @ApiModelProperty(value = "收货人")
    private String userName;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "订单id")
    private String orderId;
    @ApiModelProperty(value = "订单类型")
    private String orderTypeValue;
    @ApiModelProperty(value = "订单币种")
    private String currencyType;
    @ApiModelProperty(value = "查询内容")
    private String content;
    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
}
