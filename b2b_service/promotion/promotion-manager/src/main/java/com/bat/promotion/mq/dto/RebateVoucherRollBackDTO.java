package com.bat.promotion.mq.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/2/17 10:57
 */
@Data
public class RebateVoucherRollBackDTO implements Serializable {
    private Integer orderId;
    private BigDecimal rollBackAmount;
}


