package com.bat.promotion.service.rebatevoucher.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/2/25 13:53
 */
@Data
public class RollBackRebateSumDTO {

    /**
     * 卷id
     */
    private Integer rebateVoucherId;

    /**
     * 券号
     */
    private String rebateVoucherNo;

    /**
     * 当前余额
     */
    private BigDecimal balance;

    /**
     * 返利可退金额
     */
    private BigDecimal useAmount;

    /**
     * 结束有效期时间
     */
    private Date endTime;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 代金券状态
     */
    private Short voucherStatus;
}
