package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/8 20:44
 */
@Data
public class OrderTimerDTO implements Serializable {
    private Integer orderId;
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
}
