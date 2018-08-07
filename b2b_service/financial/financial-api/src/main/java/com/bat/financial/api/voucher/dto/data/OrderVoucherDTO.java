package com.bat.financial.api.voucher.dto.data;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 8:55
 */
@Data
public class OrderVoucherDTO implements Serializable {
    /**
     * 交易方类型： 1分销商 2 C端客户
     */
    private Short counterpartyType;
    private Integer distributorId;
    private String distributorName;
    private String companyName;
    private Short payWay;
    private String currencyType;
    private String outTradeNo;
}
