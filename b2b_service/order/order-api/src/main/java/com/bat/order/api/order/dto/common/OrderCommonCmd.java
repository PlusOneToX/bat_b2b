package com.bat.order.api.order.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderCommonCmd {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "C端客户Id")
    private Integer customerId;

    @ApiModelProperty(value = "结算币种 人民币CNY")
    @NotBlank(message = "P_ORDER_CURRENCY_TYPE_NULL")
    private String currencyType;

    @ApiModelProperty(value = "收货地址Id")
    @NotNull(message = "ORDER_DELIVERY_ADDRESS_ID_NULL")
    private Integer deliveryAddressId;

    @ApiModelProperty(value = "配送方式id")
    @NotNull(message = "ORDER_DISTRIBUTION_ID_NULL")
    private Integer distributionId;

    @ApiModelProperty(value = "订单类型表主键 ")
    @NotNull(message = "ORDER_TYPE_ID_NULL")
    private Integer orderTypeId;

    @ApiModelProperty(value = "订单来源、不能超过十个词")
    @NotBlank(message = "ORDER_SOURCE_NULL")
    private String orderSource;

    @ApiModelProperty(value = "支付方式 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付 ")
    @NotNull(message = "ORDER_PAY_WAY_NULL")
    private Short payWay;

    @ApiModelProperty(value = "订单备注")
    private String remark;

    @ApiModelProperty(value = "是否开具发票 1、是 0、否")
    @NotNull(message = "ORDER_INVOICE_FLAG_NULL")
    private Short invoiceFlag;

    @ApiModelProperty(value = "订单应付款金额、实际支付金额、两位小数")
    @NotNull(message = "ORDER_PAY_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_PAY_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal payAmount;


    @ApiModelProperty(value = "订单金额（跟payAmount一样）、两位小数")
    @NotNull(message = "ORDER_ORDER_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_ORDER_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "商品金额、两位小数")
    @NotNull(message = "ORDER_GOODS_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_GOODS_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "订单促销金额 （订单维度）、两位小数")
    @NotNull(message = "ORDER_ORDER_PROMOTION_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_PROMOTION_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal orderPromotionAmount;

    @ApiModelProperty(value = "商品促销金额和 商品维度、两位小数")
    @NotNull(message = "ORDER_GOODS_PROMOTION_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_GOODS_PROMOTION_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal goodsPromotionAmount;

    @ApiModelProperty(value = "物流费用金额、两位小数")
    @NotNull(message = "ORDER_DISTRIBUTION_AMOUNT_NULL")
    @Min(value = 0L,message = "ORDER_DISTRIBUTION_AMOUNT_CANNOT_LESS_THAN_ZERO")
    private BigDecimal distributionAmount;


    @ApiModelProperty(value = "收货地址对象")
    @Valid
    private OrderAddress orderAddress;

    @ApiModelProperty(value = "发票信息、invoiceFlag为1时必填")
    private OrderInvoiceCmd OrderInvoiceCmd;
}
