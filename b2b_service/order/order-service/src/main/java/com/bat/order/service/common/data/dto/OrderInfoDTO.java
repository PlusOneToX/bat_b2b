package com.bat.order.service.common.data.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/19 14:00
 */
@Data
@ToString
public class OrderInfoDTO {

    /**
     * 是否开具发票 0.否，1.是
     */
    private Short invoiceFlag;
    /**
     * 订单来源：平台编码 platform
     */
    private String orderSource;
    /**
     * 结算币种 人民币CNY
     */
    private String currencyType;
    /**
     * 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
     */
    private Short payWay;
    /**
     * 商品结算金额(优惠后金额，包含物流费用),不为空时需进行比较
     */
    private BigDecimal orderAmount;
    /**
     * 物流费用，不为空且不拆单时需进行比较
     */
    private BigDecimal distributionAmount;
    /**
     * 下单商品明细列表
     */
    private List<OrderGoodsDTO> goodss;
    /**
     * 配送方式列表
     */
    private List<OrderLogisticsDTO> logisticss;
    /**
     * 收货地址信息
     */
    private OrderDeliveryDTO delivery;
    /**
     * 发票信息
     */
    private OrderInvoiceDTO invoice;
    /**
     * 订单是否拆分(在途在库) 1、拆 0、否
     */
    private Short onWaySplitFlag;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 第三方客户订单编号
     */
    private String orderThirdpartyNo;

    /**
     * 店铺编码
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 是否分享 0否 1是
     */
    private Short shareFlag;

    /**
     * openid
     */
    private String openId;

    /**
     * 代金券信息
     */
    private BigDecimal rebateVoucherAmount;

    private List<Integer> rebateVoucherIds;
}
