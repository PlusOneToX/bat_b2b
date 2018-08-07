package com.bat.order.dao.cost.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderDistributorCostDO implements Serializable {
    private static final long serialVersionUID = 7269133129988672010L;
    private Integer id;

    private Integer orderId;

    private Integer distributorId;

    private BigDecimal goodsAmount;

    private BigDecimal distributionAmount;

    private BigDecimal goodsPromotionAmount;

    private BigDecimal orderPromotionAmount;

    /**
     * 返利代金券抵扣总额
     */
    private BigDecimal rebateVoucherAmount;

    private BigDecimal depositAmount;

    private BigDecimal payAmount;

    private BigDecimal platformAmount;

    private BigDecimal paidAmount;

    private BigDecimal refundedAmount;

    private String outTradeNo;

    private Date createTime;

    private Date updateTime;

    private OrderDistributorDataDO distributorDataDO;

}