package com.bat.order.dao.cost.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderDistributorCommissionDO {
    private Integer id;

    private Integer orderId;

    private Integer distributorAncestorId;

    private Integer distributorDescendantId;

    private BigDecimal amount;

    private BigDecimal orderAncestorPrice;

    private BigDecimal orderDescendantPrice;

    private Short commissionType;

    private Date createTime;

    private Date updateTime;

}