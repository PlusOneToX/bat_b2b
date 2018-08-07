package com.bat.financial.api.refund.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class OrderRefundDTO implements Serializable {

    /**
     * 接收方类型 1分销商 2 C端客户
     */
    private Short receiverType;
    /**
     * 分销商(接收方类型为1 时有值)
     */
    private Integer distributorId;
    private String distributorName;
    private String companyName;
    /**
     * C端客户（接收方类型为2 时有值）
     */
    private Integer customerId;
    private String customerName;
    /**
     * 支付接收方分销商id（退款账号为2时有值）
     */
    private Integer distributorRefundId;
    /**
     * 业务类型 1订单取消 2订单变更
     */
    private Short businessTypes;
    private Integer orderId;
    /**
     * 支付凭证号(注意：全平台唯一)
     */
    private String outTradeNo;
    /**
     * 现金支付退款金额
     */
    private BigDecimal cashAmount;
    /**
     * 余额支付退款金额
     */
    private BigDecimal depositAmount;
    /**
     * 退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式 (如果不传默认不处理退款申请单，人工处理) 4 混合退款方式
     */
    private Short refundType;
    /**
     * 退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商
     */
    private Short refundMode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 有可能为空(如ERP或第三方系统)
     */
    private Integer operatorId;
    private String operatorName;
}
