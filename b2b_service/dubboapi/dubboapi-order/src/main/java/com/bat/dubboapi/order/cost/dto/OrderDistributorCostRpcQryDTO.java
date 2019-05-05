package com.bat.dubboapi.order.cost.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDistributorCostRpcQryDTO implements Serializable {

    private static final long serialVersionUID = -3900127536945057422L;
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

    private String outTradeNo;

    private Date createTime;

    private Date updateTime;


}