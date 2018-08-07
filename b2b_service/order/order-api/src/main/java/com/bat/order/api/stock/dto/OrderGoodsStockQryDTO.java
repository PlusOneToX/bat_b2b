package com.bat.order.api.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderGoodsStockQryDTO {

    @ApiModelProperty(value = "订单商品明细id")
    private Integer orderGoodsId;

    @ApiModelProperty(value = "B2B订单id、前端拿这个做订单号")
    private Integer orderId;

    @ApiModelProperty(value = "B2B订单号、前端拿这个做订单号")
    private String orderNo;

    @ApiModelProperty(value = "ERP订单号")
    private String orderErpNo;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成")
    private Short orderStatus;

    @ApiModelProperty(value = "在库锁定数量")
    private Integer inStockLockNum;

    @ApiModelProperty(value = "在途锁定数量")
    private Integer onWayLockNum;

    @ApiModelProperty(value = "VMI锁定数量")
    private Integer vmiLockNum;


    @ApiModelProperty(value = "锁定时间")
    private Date createTime;


}