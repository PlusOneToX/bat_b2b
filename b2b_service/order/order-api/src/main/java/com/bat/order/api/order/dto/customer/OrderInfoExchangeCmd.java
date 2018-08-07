package com.bat.order.api.order.dto.customer;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bat.order.api.order.dto.distributor.OrderLogisticsCmd;
import com.bat.order.api.order.dto.common.OrderInvoiceCmd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "兑换下单信息")
public class OrderInfoExchangeCmd {
    @ApiModelProperty(value = "是否开具发票 0.否，1.是(不填、默认是0)", required = false, example = "0")
    private Short invoiceFlag;
    @ApiModelProperty(value = "结算币种 人民币CNY(不填、默认为CNY)", required = false, example = "CNY")
    private String currencyType;
    @ApiModelProperty(value = "付款方式 10.兑换,11 兑换+支付宝,12 兑换+微信", required = true, example = "1")
    @NotNull(message = "P_ORDER_PAY_WAY_NULL")
    private Short payWay;
    @ApiModelProperty(value = "商品结算金额(优惠后金额)", required = true, example = "15.65")
    @NotNull(message = "P_ORDER_ORDER_AMOUNT_NULL")
    private BigDecimal orderAmount;
    @ApiModelProperty(value = "物流费用", required = true, example = "15.65")
    @NotNull(message = "P_ORDER_DISTRIBUTION_AMOUNT_NULL")
    private BigDecimal distributionAmount;
    @ApiModelProperty(value = "订单是否拆分(在途在库) 1、拆 0、否（默认为否）", required = false, example = "0")
    private Short onWaySplitFlag;
    @ApiModelProperty(value = "订单备注", required = false, example = "0")
    private String remark;
    @ApiModelProperty(value = "店铺编码", required = false, example = "4666")
    private String shopCode;
    @ApiModelProperty(value = "店铺名称", required = false, example = "bat")
    private String shopName;

    @NotNull(message = "P_ORDER_GOODS_NULL")
    @Valid
    @ApiModelProperty(value = "下单商品明细列表", required = true)
    private List<OrderGoodsExchangeCmd> goodss;

    @NotNull(message = "P_ORDER_LOGISTICS_NULL")
    @Valid
    @ApiModelProperty(value = "配送方式列表", required = true)
    private List<OrderLogisticsCmd> logisticss;

    @NotNull(message = "P_ORDER_DELIVERY_NULL")
    @Valid
    @ApiModelProperty(value = "收货地址信息", required = true)
    private OrderDeliveryCmd delivery;

    @Valid
    @ApiModelProperty(value = "发票信息", required = true)
    private OrderInvoiceCmd invoice;

    @ApiModelProperty(value = "是否分享 0否 1是", required = true)
    private Short shareFlag;
}