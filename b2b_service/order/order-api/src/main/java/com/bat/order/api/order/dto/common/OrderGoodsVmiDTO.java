package com.bat.order.api.order.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 沙漠
 */
@Data
public class OrderGoodsVmiDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("订单号")
    private Integer orderId;

    @ApiModelProperty("货品id")
    private Integer itemId;

    @ApiModelProperty("货品编号")
    private String itemCode;

    @ApiModelProperty("货品名称")
    private String itemName;

    @ApiModelProperty("vmi货品数量")
    private Integer itemVmiCount;

    @ApiModelProperty("erp订单编号")
    private String orderErpNo;

    @ApiModelProperty("订单下单时间")
    private Date orderCreateTime;

    @ApiModelProperty("付款状态")
    private Short payStatus;

    @ApiModelProperty("订单状态")
    private Short orderStatus;
}
