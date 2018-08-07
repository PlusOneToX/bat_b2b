package com.bat.financial.api.deposit.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 19:45
 */
@Data
public class ReceiptPayBillsQry {
    private Integer distributorId;
    private Short businessType;
    private BigDecimal totalFee;
    private Short payType;
    private Short payStatus;
    private Date payTime;
    private Short tradeMode = 1;
}
