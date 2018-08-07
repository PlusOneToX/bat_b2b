package com.bat.order.mq.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class OrderPayDTO implements Serializable {
    /**
     * ALIPAY_BIZ_VOUCHER：商家全场券。
     */
    public static final String ALIPAY_VOUCHER_TYPE_ALIPAY_BIZ_VOUCHER = "ALIPAY_BIZ_VOUCHER";

    /**
     * ALIPAY_VOUCHER_TYPE_ALIPAY_COMMON_ITEM_VOUCHER：商家单品券。
     */
    public static final String ALIPAY_VOUCHER_TYPE_ALIPAY_COMMON_ITEM_VOUCHER =
        "ALIPAY_VOUCHER_TYPE_ALIPAY_COMMON_ITEM_VOUCHER";

    /**
     * ALIPAY_VOUCHER_TYPE_ALIPAY_CASH_VOUCHER：平台优惠券，支付宝或第三方出资。。
     */
    public static final String ALIPAY_VOUCHER_TYPE_ALIPAY_CASH_VOUCHER = "ALIPAY_VOUCHER_TYPE_ALIPAY_CASH_VOUCHER";

    /**
     * ALIPAY_VOUCHER_TYPE_ALICREDIT_INTFREE_VOUCHER：花呗分期券，该券仅做订单外的工作呗分期费用减免，并不抵扣订单内支付金额。。。
     */
    public static final String ALIPAY_VOUCHER_TYPE_ALICREDIT_INTFREE_VOUCHER =
        "ALIPAY_VOUCHER_TYPE_ALICREDIT_INTFREE_VOUCHER";

    private List<Integer> orderIds;
    /**
     * 交易方类型： 1分销商 2 C端客户
     */
    private Short counterpartyType;
    /**
     * 交易类型为1时必填
     */
    private Integer distributorId;
    /**
     * 交易类型为2时必填
     */
    private Integer customerId;
    /**
     * 余额支付金额
     */
    private BigDecimal depositAmount;
    /**
     * 现金支付金额
     */
    private BigDecimal paidAmount;
    /**
     * 支付时间
     */
    private Date padTime;
    /**
     * 支付凭证号
     */
    private String outTradeNo;
    /**
     * 实际收款金额（支付宝 无资金商户优惠券 这种逻辑下会有值）
     */
    private BigDecimal receiptAmount;

    /**
     * 支付宝优惠券类型
     */
    private String alipayVoucherType;

    /**
     * 优惠金额
     */
    private BigDecimal diffAmount;

    /**
     * 全场优惠券类型
     */
    private Boolean alipayVoucherBizFlag;
}
