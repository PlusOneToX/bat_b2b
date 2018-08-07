package com.bat.financial.mq.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class OrderCommissionDTO implements Serializable {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 分销商id
     */
    private Integer distributorId;
    /**
     * 佣金金额（可能存在负数）
     */
    private BigDecimal amount;
    /**
     * 佣金类型 1.增加，2.减少
     */
    private Short commissionType;

}
