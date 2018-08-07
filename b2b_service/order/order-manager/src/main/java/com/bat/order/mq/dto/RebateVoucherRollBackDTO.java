package com.bat.order.mq.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/2/17 10:57
 */
@Data
public class RebateVoucherRollBackDTO implements Serializable {
    private Integer orderId;
    private BigDecimal rollBackAmount;
}


