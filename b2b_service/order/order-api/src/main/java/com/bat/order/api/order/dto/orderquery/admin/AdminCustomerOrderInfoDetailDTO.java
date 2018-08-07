package com.bat.order.api.order.dto.orderquery.admin;

import java.util.List;

import com.bat.order.api.deliver.dto.data.OrderDeliveryDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderCustomerDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDetailGoodsDTO;
import com.bat.order.api.cost.dto.data.OrderInvoiceDTO;
import com.bat.order.api.data.dto.data.OrderExtendDataDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderInfoDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 柔性订单 订单详情
 */
@Data
public class AdminCustomerOrderInfoDetailDTO {

    /**
     * 前端订单状态
     */
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;

    /**
     * 订单基本信息
     */
    @ApiModelProperty("订单基本信息")
    private OrderInfoDTO orderInfo;
    /**
     * 订单物流信息
     */
    @ApiModelProperty("订单物流信息")
    private OrderDeliveryDTO orderDelivery;
    /**
     * 订单扩展信息
     */
    @ApiModelProperty("订单扩展信息")
    private OrderExtendDataDTO orderExtendData;
    /**
     * 默认分销商信息
     */
    @ApiModelProperty("默认分销商信息")
    private OrderCustomerDTO orderDistributor;
    /**
     * 默认分销商商品信息
     */
    @ApiModelProperty("默认分销商商品信息")
    private List<OrderInfoDetailGoodsDTO> orderInfoDetailGoods;

    /**
     * 默认发票信息
     */
    @ApiModelProperty("默认发票信息")
    private OrderInvoiceDTO orderInvoice;
    //
    // /**
    // * 订单所有等级分销商信息
    // */
    // @ApiModelProperty("订单所有等级分销商信息")
    // private List<OrderDistributorDTO> orderDistributors;
    //
    // /**
    // * 订单所有等级分销商商品信息
    // */
    // @ApiModelProperty("订单所有等级分销商商品信息")
    // private List<OrderInfoDetailGoodsDTO> orderInfoDetailGoodsList;
}