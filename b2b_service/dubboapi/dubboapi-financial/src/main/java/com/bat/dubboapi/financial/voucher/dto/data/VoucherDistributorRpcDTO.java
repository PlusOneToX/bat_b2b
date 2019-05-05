package com.bat.dubboapi.financial.voucher.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/23 13:40
 */
@Data
public class VoucherDistributorRpcDTO implements Serializable {
    private Integer id;

    /**
     * 分销商id
     */
    private Integer distributorId;

    /**
     * 分销商用户名
     */
    private String distributorName;

    /**
     * 分销商公司名
     */
    private String companyName;

    /**
     * 收款金额
     */
    private BigDecimal amount;

    /**
     * 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付
     */
    private Short payWay;

    /**
     * 分销客户支付凭证id(现款线上支付情况才有值)
     */
    private Integer payBillsId;

    /**
     * C端客户标志 0 不是C端客户(默认B2B) 1是C端客户
     */
    private Short customerFlag;

    /**
     * 币种
     */
    private String currencyType;

    /**
     * 业务类型 1订单收款2在线充值收款
     */
    private Short businessType;

    /**
     * 业务单号(业务类型为1时为订单id)
     */
    private String businessId;

    /**
     * 收款单状态,1待确认,2已确认,3已取消
     */
    private Short voucherStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * erp收款单编号
     */
    private String voucherErpNo;

    /**
     * 支付凭证号
     */
    private String outTradeNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
